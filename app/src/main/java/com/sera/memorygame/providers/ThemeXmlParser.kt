package com.sera.memorygame.providers

import android.content.Context
import android.graphics.Color
import org.xml.sax.Attributes
import org.xml.sax.InputSource
import org.xml.sax.SAXException
import org.xml.sax.XMLReader
import org.xml.sax.helpers.DefaultHandler
import java.io.File
import java.io.IOException
import javax.xml.parsers.ParserConfigurationException
import javax.xml.parsers.SAXParser
import javax.xml.parsers.SAXParserFactory

class ThemeXmlParser(val context: Context) {

    private var colorMap: Map<String, Int>? = null
    private var configMap: Map<String, String>? = null


    fun newInstance(ctx: Context): ThemeXmlParser? {
        return ThemeXmlParser(ctx)
    }

    private fun selectAssetsPath(scheme: String): String? {
        var path = ""
//        if (scheme == XMLStr.Theme.google_dark) {
//            path =context?.filesDir.toString() + "/assets/files/values/theme.xml"
//        } else {
//            //todo add other skins
//        }
        val assetsPath = context.filesDir.toString() + "/assets/files/values/"
        println()
//        val file = File(assetsPath + "theme.xml")
        return assetsPath
    }

    fun parse(scheme: String) {
//        val assetsPath = selectAssetsPath(scheme)
//        var `is`: InputSource? = null
//        try {
//            `is` = InputSource(assetsPath?.let { context?.assets?.open(it) })
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
        val assetsPath = context.filesDir.toString() + "/assets/files/values/"
        println()
        val file = File(assetsPath + "theme.xml")
        println()
//        if (`is` != null) {
            doParse(InputSource(file.inputStream()))
//        }
    }

    fun colorMap(): Map<String, Int>? {
        return colorMap
    }

    fun configMap(): Map<String, String>? {
        return configMap
    }


    private fun doParse(`is`: InputSource) {
        val xmlReader: XMLReader? = createXMLReader()
        val colorHandler = ColorHandler(context = context)
        xmlReader?.contentHandler = colorHandler
        try {
            xmlReader?.parse(`is`)
        } catch (e: SAXException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun createXMLReader(): XMLReader? {
        var reader: XMLReader? = null
        val saxParser: SAXParser? = createSAXParser()
        try {
            reader = saxParser?.xmlReader
        } catch (e: SAXException) {
            e.printStackTrace()
        }
        return reader
    }

    private fun createSAXParser(): SAXParser? {
        val factory: SAXParserFactory = SAXParserFactory.newInstance()
        var saxParser: SAXParser? = null
        try {
            saxParser = factory.newSAXParser()
        } catch (e: ParserConfigurationException) {
            e.printStackTrace()
        } catch (e: SAXException) {
            e.printStackTrace()
        }
        return saxParser
    }


    internal class ColorHandler(val context: Context) : DefaultHandler() {
        @Throws(SAXException::class)
        override fun startElement(
            uri: String?, localName: String?, qName: String?,
            attrs: Attributes
        ) {
            if (attrs.length<2){
                return
            }
            val name: String = attrs.getValue(0)
            val value: String = attrs.getValue(1)

            try {
                if (value.startsWith("#")) {
                    //color
                    val color = toColor(value)
//                    colorMap.put(name, color)
                    println()
                } else {
                    //config
                    //configMap.put(name, value)
                    println()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        /**
         * @param colorStr: #FFFFFF
         * @return:Color.rgb(256, 256, 256)
         */
        private fun toColor(colorStr: String): Int {
            return Color.parseColor(colorStr)
        }
    }

}