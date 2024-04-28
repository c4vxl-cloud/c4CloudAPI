package de.c4vxl.api

import org.json.JSONArray
import org.json.JSONObject
import java.io.DataOutputStream
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

data class CloudError(val error: Int, val description: String?): Exception("c4vxl cloud has thrown an Exception!\nError Code: $error\n${if (description != null) "Error Message: $description" else ""}")

class c4CloudAPI(var apiKey: String) {
    companion object {
        var apiURL: URL = URL("https://cloud.c4vxl.de/cloud/api/")
    }

    // functions for api endpoints
    fun filesystemList(path: String): MutableList<Any> {
        val response = executeRequest("filesystem_list", mutableMapOf("path" to path))

        return (response["content"] as? JSONArray)?.toMutableList()
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun filesystemFileHash(path: String): String {
        val response = executeRequest("filesystem_file_hash", mutableMapOf("path" to path))

        return response["content"] as? String
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun filesystemDownload(path: String, dest: String): File {
        val content: String = this.filesystemGetFileContent(path)

        File(dest).let {
            it.createNewFile()
            it.writeBytes(content.toByteArray())

            return it
        }
    }

    fun filesystemUpload(dest: String, file: File): Boolean {
        val response = executeRequest("filesystem_upload", mutableMapOf("file" to file, "dest" to dest))

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun filesystemCreateFile(path: String): Boolean {
        val response = executeRequest("filesystem_create_file", mutableMapOf("path" to path))

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun filesystemCreateFolder(path: String): Boolean {
        val response = executeRequest("filesystem_create_folder", mutableMapOf("path" to path))

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun filesystemDelete(path: String): Boolean {
        val response = executeRequest("filesystem_delete", mutableMapOf("path" to path))

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun filesystemCompile(path: String): Boolean {
        val response = executeRequest("filesystem_compile", mutableMapOf("path" to path))

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun filesystemDecompile(path: String): Boolean {
        val response = executeRequest("filesystem_decompile", mutableMapOf("path" to path))

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun filesystemRename(path: String, newName: String): Boolean {
        val response = executeRequest("filesystem_rename", mutableMapOf("path" to path, "newName" to newName))

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun filesystemMove(path: String, dest: String): Boolean {
        val response = executeRequest("filesystem_move", mutableMapOf("path" to path, "dest" to dest))

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun filesystemSetFileContent(path: String, newContent: String): Boolean {
        val response = executeRequest("filesystem_set_file_content", mutableMapOf("path" to path, "newContent" to newContent))

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun filesystemGetFileContent(path: String): String {
        val response = executeRequest("filesystem_get_file_content", mutableMapOf("path" to path))

        return response["content"] as? String ?: ""
    }

    fun isAPIKeyValid(): Boolean {
        val response = executeRequest("filesystem_is_valid")

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun accountRegister(username: String, password: String): Boolean {
        val response = executeRequest("account_register", mutableMapOf("username" to username, "password" to password))

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun accountUnregister(): Boolean {
        val response = executeRequest("account_unregister")

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun accountSetUsername(newName: String): Boolean {
        val response = executeRequest("account_set_username", mutableMapOf("newName" to newName))

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun accountSetPassword(newPassword: String): Boolean {
        val response = executeRequest("account_set_password", mutableMapOf("newPassword" to newPassword))

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun accountInfo(uuid: String): Map<String, Any> {
        val response = executeRequest("account_info")

        return (response["content"] as? JSONObject)?.toMap()?.toMutableMap()
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun accountSetStatus(newStatus: String): Boolean {
        val response = executeRequest("account_set_status", mutableMapOf("newStatus" to newStatus))

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun accountGetStatus(): String {
        val response = executeRequest("account_get_status")

        return response["content"] as? String
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun accountUploadProfilePicture(file: File): Boolean {
        val response = executeRequest("account_upload_profile_picture", mutableMapOf("file" to file))

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun accountGetProfilePicture(): String {
        val response = executeRequest("account_get_profile_picture")

        return "${apiURL.protocol}://${apiURL.host}/${(response["content"] as? String) ?: throw CloudError(-1, "Got an unexpected response from api")}"
    }

    fun backupsList(): MutableList<Any> {
        val response = executeRequest("backups_list")

        return (response["content"] as? JSONArray)?.toMutableList()
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun backupsCreate(): Boolean {
        val response = executeRequest("backups_create")

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun backupsRemove(backupId: String): Boolean {
        val response = executeRequest("backups_remove", mutableMapOf("backupsId" to backupId))

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun backupsLoad(backupId: Int): Boolean {
        val response = executeRequest("backups_load", mutableMapOf("backupsId" to backupId))

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun friendsList(): List<Map<Any, Any>> {
        val response = executeRequest("friends_list")

        return ((response["content"] as? JSONArray)?.toMutableList() as? List<Map<Any, Any>>)
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun friendsListRequests(): List<Map<Any, Any>> {
        val response = executeRequest("friends_list_requests")

        return ((response["content"] as? JSONArray)?.toMutableList() as? List<Map<Any, Any>>)
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun friendsSendRequest(to: String): Boolean {
        val response = executeRequest("friends_send_request", mutableMapOf("to" to to))

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun friendsAcceptRequest(from: String): Boolean {
        val response = executeRequest("friends_accept_request", mutableMapOf("from" to from))

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun friendsDenyRequest(from: String): Boolean {
        val response = executeRequest("friends_deny_request", mutableMapOf("from" to from))

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun friendsRemove(friend: String): Boolean {
        val response = executeRequest("friends_remove", mutableMapOf("friend" to friend))

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun fileshareList(): List<Map<Any, Any>> {
        val response = executeRequest("fileshare_list")

        return ((response["content"] as? JSONArray)?.toMutableList() as? List<Map<Any, Any>>)
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun fileshareShare(path: String, with: String): Boolean {
        val response = executeRequest("fileshare_share", mutableMapOf("path" to path, "with" to with))

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun fileshareIgnore(id: Int): Boolean {
        val response = executeRequest("fileshare_ignore", mutableMapOf("id" to id))

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun fileshareSave(id: String): Boolean {
        val response = executeRequest("fileshare_save", mutableMapOf("id" to id))

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun messagesList(): List<Any> {
        val response = executeRequest("messages_list")

        return ((response["content"] as? JSONArray)?.toMutableList())
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun messagesDelete(id: String): Boolean {
        val response = executeRequest("messages_delete", mutableMapOf("id" to id))

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }

    fun messagesSetRead(id: String): Boolean {
        val response = executeRequest("messages_set_read", mutableMapOf("id" to id))

        return response["success"] as? Boolean
            ?: throw CloudError(-1, "Got an unexpected response from api")
    }




    // logic for sending requests to api
    fun executeRequest(request: String, vars: MutableMap<String, Any> = mutableMapOf()): MutableMap<Any, Any> {
        val responseString: String = with(apiURL.openConnection() as HttpURLConnection) { // open connection to api
            // configure connection to api
            doOutput = true
            doInput = true
            useCaches = false
            requestMethod = "POST"
            setRequestProperty("Content-Type", "multipart/form-data; boundary=*****")

            DataOutputStream(outputStream).use {
                // add api_key and request to vars
                vars["api_key"] = apiKey
                vars["request"] = request

                // send vars
                vars.forEach { (key, value) ->
                    it.writeBytes("--*****\r\n")

                    when(value) {
                        is File -> { // handle when var is a file
                            it.writeBytes("Content-Disposition: form-data; name=\"$key\"; filename=\"$value\"\r\n")
                            it.writeBytes("\r\n")
                            value.inputStream().transferTo(it)
                            it.writeBytes("\r\n")
                        }
                        else -> { // handle else-case
                            it.writeBytes("Content-Disposition: form-data; name=\"$key\"\r\n")
                            it.writeBytes("\r\n")
                            it.writeBytes("$value\r\n")
                        }
                    }
                }

                it.writeBytes("--*****--\r\n") // send end of data

                it.flush() // flush data
                it.close() // close connection
            }

            inputStream.bufferedReader().use { it.readText() } // read response from api as String
        }

        val jsonPart: String = responseString.extractJSON ?: "{\"content\": \"$responseString\"}" // get json part of string

        val response = mutableMapOf<Any, Any>().apply {
            // add all parts of json object to list
            JSONObject(jsonPart).let {it.keySet().forEach { key ->this[key] = it.get(key) } }
        }

        // handle errors from api
        response["error"]?.toString()?.toIntOrNull()?.let { errorCode -> throw CloudError(errorCode, response["error_message"]?.toString()) }

        return response
    }

    private val String.extractJSON: String? get() {
        val start = this.indexOf("{")
        val end = this.lastIndexOf("}")

        if (start == -1 || end == -1) return null

        return this.substring(start, end + 1)
    }
}