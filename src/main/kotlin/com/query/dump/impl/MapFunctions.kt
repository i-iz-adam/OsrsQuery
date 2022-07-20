package com.query.dump.impl

import SpriteData
import com.query.Application
import com.query.Application.areas
import com.query.Application.objects
import com.query.Constants
import com.query.dump.DefinitionsTypes
import com.query.dump.TypeManager
import com.query.utils.FileUtils.getFile
import com.query.utils.IndexType
import com.query.utils.progress
import com.query.utils.revisionBefore
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.IOException
import java.nio.ByteBuffer
import javax.imageio.ImageIO


class MapFunctions : TypeManager {

    override val requiredDefs = listOf(
        DefinitionsTypes.AREAS,
        DefinitionsTypes.SPRITES,
        DefinitionsTypes.OBJECTS
    )

    override fun load() {
        writeImage()
    }

    override fun onTest() {
        Sprites().load()
        writeImage()
    }

    private fun writeImage() {
        val functions = getMapFunctions()
        val progress = progress("Writing Area Sprites", functions.size.toLong())
        var index = 0
        areas().filter { it.spriteId != -1 }.forEach {
            val pink = getFile("sprites/pink/", "${it.spriteId}.png")
            var buffImg = BufferedImage(15, 15, BufferedImage.TYPE_INT_ARGB)
            try {
                buffImg = ImageIO.read(pink)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            ImageIO.write(buffImg, "png", getFile("mapFunctions/","${it.spriteId}.png"))
            progress.step()
            index++
        }
        progress.close()
    }



    companion object {

        fun getMapFunctions() : Map<Int,BufferedImage> {
                val data : MutableMap<Int,BufferedImage> = emptyMap<Int, BufferedImage>().toMutableMap()

                val functions = areas().filter { it.spriteId != -1 }
                functions.filter { it.spriteId != -1 }.forEachIndexed { index, area ->
                    val pink = getFile("sprites/pink/", "${area.spriteId}.png")
                    var buffImg = BufferedImage(15, 15, BufferedImage.TYPE_INT_ARGB)
                    try {
                        buffImg = ImageIO.read(pink)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    data[index] = buffImg
            }
            return data
        }

        @JvmStatic
        fun main(args : Array<String>) {
            val parser = ArgParser("app")
            val rev by parser.option(ArgType.Int, description = "The revision you wish to dump").default(0)
            parser.parse(args)
            Application.revision = 1

            MapFunctions().test()
        }
    }

}