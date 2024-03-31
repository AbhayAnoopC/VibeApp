package com.chatapp.vibeapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chatapp.vibeapp.data.Contact
import kotlinx.coroutines.launch
import android.content.Context
import android.provider.ContactsContract
import com.chatapp.vibeapp.data.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class ContactsViewModel(application: Application) : AndroidViewModel(application) {
    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> = _contacts

    private val _navigationEvent = MutableLiveData<String?>()
    val navigationEvent: LiveData<String?> = _navigationEvent

    private val chatRepository = ChatRepository(firestore = firebase.firestore.)
//    fun loadContacts() {
//        viewModelScope.launch {
//            val fetchedContacts = fetchDeviceContacts(getApplication<Application>().applicationContext)
//            // Here, you could also update `isUser` by checking against Firebase
//            _contacts.postValue(fetchedContacts)
//        }
//    }
    fun loadContacts() {
        viewModelScope.launch {
            val fetchedContacts = fetchDeviceContacts(getApplication<Application>().applicationContext)
            updateContactsUserStatus(fetchedContacts)
        }
    }

    fun updateContactsUserStatus(contacts: List<Contact>) {
        viewModelScope.launch {
            contacts.forEach { contact ->
                val userExists = checkUserExistsInFirebase(contact.phoneNumber)
                contact.isUser = userExists // Assuming you can modify isUser or recreate the contact object

            }
            //_contacts.postValue(contacts)
            _contacts.postValue(contacts.sortedBy { it.name })

        }
    }

    fun initiateChatWithContact(currentUser: String, contactPhoneNumber: String) {
        viewModelScope.launch {
            // Ensure you have implemented getOrCreateChatSessionId in your ChatRepository
            val chatSessionId = chatRepository.getOrCreateChatSessionId(currentUser, contactPhoneNumber)
            _navigationEvent.postValue(chatSessionId)
        }
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }

}


fun cleanPhoneNumber(phoneNumber: String): String {
    // Remove anything that is not a digit
    return phoneNumber.replace(Regex("[^\\d]"), "")
}

suspend fun fetchDeviceContacts(context: Context): List<Contact> = withContext(Dispatchers.IO) {
    val contactsList = mutableListOf<Contact>()
    val cursor = context.contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER),
        null,
        null,
        null
    )

    cursor?.use {
        val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

        while (it.moveToNext()) {
            val name = it.getString(nameIndex)
            var number = it.getString(numberIndex)
            number = cleanPhoneNumber(number)
            contactsList.add(Contact(name, number, isUser = false)) // Initial assumption, needs checking against Firebase
        }
    }

    return@withContext contactsList
}

suspend fun checkUserExistsInFirebase(phoneNumber: String): Boolean {
    val db = Firebase.firestore
    return try {
        // Assuming 'users' is the collection where user documents are stored
        val querySnapshot = db.collection("users")
            .whereEqualTo("phoneNumber", phoneNumber)
            .limit(1) // only need to check if at least one document exists
            .get()
            .await()

        !querySnapshot.isEmpty // True if a matching user is found, false otherwise
    } catch (e: Exception) {
        false
    }
}



