package com.gtera.utils

import android.util.Log
import com.gtera.BuildConfig
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class Logger private constructor() {
    private val loggerFileName = "Base_Logger.txt"
    private val write: BufferedWriter? = null
    private val canWrite = false
    private val limitedFileSize = 500000
    private val root: File? = null
    private val toWriteFile: File? = null
    val loggingTime: String
        get() {
            var currentTime = ""
            val cal = Calendar.getInstance()
            val sdf =
                SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.US)
            currentTime = sdf.format(cal.time)
            return currentTime
        }


    fun i(
        tag: String?,
        msg: Any,
        writeToFile: Boolean = false
    ) { //        if (BuildConfig.DEBUG)
        Log.i(tag, msg.toString() + "")
    }


    fun v(
        tag: String?,
        msg: Any,
        writeToFile: Boolean = false
    ) { //        if (BuildConfig.DEBUG)
        Log.v(tag, msg.toString() + "")

    }

    fun e(tag: String?, msg: Any, writeToFile: Boolean) {
        if (BuildConfig.DEBUG) Log.e(tag, msg.toString() + "")
        //		Log.e(tag, msg + "");
//FirebaseCrash.report(new Exception(msg.toString()));
    }

    fun logFullMessage(tag: String?, message: Any) {
        if (!Utilities.isNullString(message.toString()) && message.toString().length > 4000) {
            val chunkCount = message.toString().length / 4000 // integer division
            for (i in 0..chunkCount) {
                val max = 4000 * (i + 1)
                if (max >= message.toString().length) {
                    v(tag, message.toString().substring(4000 * i), false)
                } else {
                    v(tag, message.toString().substring(4000 * i, max), false)
                }
            }
            return
        }
        v(tag, message.toString() + "", false)
    }

    companion object {
        private var _instance: Logger? = null
        @JvmStatic
        fun instance(): Logger? {
            if (_instance == null) _instance =
                Logger()
            return _instance
        }

        fun updateLogFile(fileName: File, newLength: Int) {
            var write: BufferedWriter? = null
            var randomAccessFile: RandomAccessFile? = null
            try {
                randomAccessFile = RandomAccessFile(fileName, "rw")
                val length = randomAccessFile.length()
                if (length > newLength) {
                    println(getFileSize(fileName).toString() + " " + length)
                    // randomAccessFile.setLength(length - 50 );
                    randomAccessFile.seek(newLength.toLong())
                    // Declare a buffer with the same length as the second line
                    val buffer = ByteArray(length.toInt() - newLength)
                    // Read data from the file
                    randomAccessFile.read(buffer)
                    // Print out the buffer contents
                    val newStr = String(buffer)
                    println(newStr)
                    randomAccessFile.close()
                    val temp = newStr.split("\n").toTypedArray()
                    println("temp " + temp.size)
                    write = BufferedWriter(
                        OutputStreamWriter(
                            FileOutputStream(fileName)
                        )
                    )
                    write.write(newStr.replaceFirst(temp[0].toRegex(), ""))
                    write.flush()
                    write.close()
                }
                randomAccessFile.close()
                // serverConnection();
//        } catch (FileNotFoundException ex) {
//			Log.v("UpdateFile", ex.getMessage());
            } catch (ex: IOException) { //			Log.v("UpdateFile", ex.getMessage());
            }
        }

        fun getFileSize(file: File): Long {
            if (!file.exists() || !file.isFile) {
                println("File doesn\'t exist")
                return -1
            }
            // Here we get the actual size
            return file.length()
        }
    }
}