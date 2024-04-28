package de.c4vxl

import de.c4vxl.api.c4CloudAPI
import java.io.File

fun main() {
    val api = c4CloudAPI("<your_api_key>")

    println(api.filesystemGetFileContent("test/wow.txt"))
    val uploadSuccess = api.filesystemUpload("test/", File(".gitignore"))
    if (uploadSuccess) println("File uploaded successfully!")
    api.filesystemDownload("test/wow.txt", "out.txt")
}