package com.sera.memorygame.utils

import android.util.Log
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

object ZipManager {

    private val BUFFER_SIZE = 6 * 1024


    fun zip(files: Array<String?>, zipFile: String) {
        var origin: BufferedInputStream?
        val out = ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile)))
        out.use { stream ->
            val data = ByteArray(BUFFER_SIZE)

            for (i in files.indices) {
                val fi = FileInputStream(files[i]!!)
                origin = BufferedInputStream(fi, BUFFER_SIZE)
                try {
                    val entry = ZipEntry(files[i]!!.substring(files[i]!!.lastIndexOf("/") + 1))
                    stream.putNextEntry(entry)

                    while (origin!!.read(data, 0, BUFFER_SIZE) != -1) {
                        stream.write(data, 0, origin!!.read(data, 0, BUFFER_SIZE))
                    }
                } catch (e: Exception) {
                    Log.d("", "")
                } finally {
                    origin!!.close()
                }
            }
        }

    }


    fun main() {
        val sourceFile = "test1.txt"
        val fos = FileOutputStream("compressed.zip")
        val zipOut = ZipOutputStream(fos)
        val fileToZip = File(sourceFile)
        val fis = FileInputStream(fileToZip)
        val zipEntry = ZipEntry(fileToZip.name)
        zipOut.putNextEntry(zipEntry)
        val bytes = ByteArray(1024)
        while ((fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, (fis.read(bytes)))
        }
        zipOut.close()
        fis.close()
        fos.close()
    }

    @Throws(IOException::class)
    fun unzip(zipFilePath: String, targetPath: String): Boolean {
        val `is`: InputStream
        val zis: ZipInputStream
        try {
            var filename: String
            `is` = FileInputStream(zipFilePath)
            zis = ZipInputStream(BufferedInputStream(`is`))
            var ze: ZipEntry?
            val buffer = ByteArray(1024)
            var count: Int

            do {
                ze = zis.nextEntry
//                println("DOWNLOAD: filename - ${ze.name}")
                if (ze == null) {
                    break
                }
                filename = ze.name

                //Split on the '/' to separate folder name from file name
                val filenames = filename.split("/")

                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory) {
                    val fmd = File("$targetPath/$filename")
                    fmd.mkdirs()
                    continue
                    //If the parent folder is included in the filepath, create a dir out of the parent
                } else if (filenames.size == 2) {
                    val fmd = File("$targetPath/${filenames[0]}")
                    fmd.mkdirs()
                }

                val fout = try {
                    FileOutputStream("$targetPath/$filename")
                } catch (e: Exception) {
                    continue
                }

                do {
                    count = zis.read(buffer)
                    if (count == -1) {
                        break
                    }
                    fout.write(buffer, 0, count)
                } while (true)

                fout.close()
                zis.closeEntry()
            } while (true)
            zis.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }

        return true
    }

    /**
     *
     */
    fun getMaxCount(file: File): Int = ZipFile(file.path).size()


}