package com.query

import com.google.gson.GsonBuilder
import com.query.Constants.properties
import com.query.cache.definitions.Definition
import com.query.cache.definitions.impl.*
import com.query.cache.definitions.impl.MapSceneProvider
import com.query.cache.download.CacheInfo
import com.query.cache.download.CacheLoader
import com.query.dump.dumper317.Dumper
import com.query.dump.impl.*
import com.query.utils.DumpAll
import com.query.utils.TimeUtils
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import mu.KotlinLogging
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.system.measureTimeMillis


object Application {

    /**
     * What Revision the user wants to dump.
     */
    var revision : Int = 0

    /**
     * Main Logger for the Application.
     */
    val logger = KotlinLogging.logger {}

    /**
     * Cache Revision data for the Revision that user requests.
     */

    lateinit var cacheInfo : CacheInfo

    var gson = GsonBuilder().setPrettyPrinting().create()

    fun initialize(rev : Int) {
        val time = measureTimeMillis {

            revision = rev

            CacheLoader.initialize()

            //Latch is necessary.
            val latch = CountDownLatch(11)

            SpriteProvider(latch,false).run()


            Sprites().load()
            //MapFunctions().load()
            //MapSceneDumper().load()
            Dumper.dumpAll()

        }

        logger.info { "Dump Completed in ${TimeUtils.millsToFormat(time)}" }

    }

    fun loadProperties() {
        logger.info { "Loading properties..." }
        val file = File("./app.properties")
        if(!file.exists()) {
            file.createNewFile()
        }
        properties.load(file.inputStream())
    }

    fun saveProperties(p: Properties) {
        val path = "./app.properties"
        val fr = FileOutputStream(path)
        p.store(fr, "Properties")
        fr.close()
    }

    /**
     * Cached definitions provided from the cache library.
     */
    val definitions: ConcurrentHashMap<Class<out Definition>, List<Definition>> = ConcurrentHashMap()


    /**
     * Prompts the application console with performance numbers.
     */
    fun prompt(command: Class<out Runnable>, start: Long) {
        //logger.debug { String.format("%s took %sms to cache.", command.simpleName, System.currentTimeMillis() - start) }
    }

    /**
     * Stores a provided list of definitions.
     */
    fun store(clazz: Class<out Definition>, list: List<Definition>) {
        definitions[clazz] = list
    }

    /**
     * Gets the textures definitions.
     */
    fun textures(): List<TextureDefinition> {
        return definitions[TextureDefinition::class.java]?.filterIsInstance<TextureDefinition>()?: error("Texture Definitions not loaded.")
    }
    fun mapScene(): List<MapSceneDefinition> {
        return definitions[MapSceneDefinition::class.java]?.filterIsInstance<MapSceneDefinition>()?: error("Map Scenes not loaded.")
    }


    /**
     * Gets the sprites definitions.
     */
    fun sprites(): List<SpriteDefinition> {
        return definitions[SpriteDefinition::class.java]?.filterIsInstance<SpriteDefinition>()?: error("Sprite Definitions not loaded.")
    }

    /**
     * Gets the objects definitions.
     */
    fun objects(): List<ObjectDefinition> {
        return definitions[ObjectDefinition::class.java]?.filterIsInstance<ObjectDefinition>()?: error("Object Definitions not loaded.")
    }

    /**
     * Gets the areas definitions.
     */
    fun areas(): List<AreaDefinition> {
        return definitions[AreaDefinition::class.java]?.filterIsInstance<AreaDefinition>()?: error("Area Definitions not loaded.")
    }


    /**
     * Gets the item definitions.
     */
    fun items(): List<ItemDefinition> {
        return definitions[ItemDefinition::class.java]?.filterIsInstance<ItemDefinition>()?: error("Item Definitions not loaded.")
    }

    /**
     * Gets the kits definitions.
     */
    fun kits(): List<KitDefinition> {
        return definitions[KitDefinition::class.java]?.filterIsInstance<KitDefinition>()?: error("Kit Definitions not loaded.")
    }

    /**
     * Gets the npc definitions.
     */
    fun npcs(): List<NpcDefinition> {
        return definitions[NpcDefinition::class.java]?.filterIsInstance<NpcDefinition>()?: error("Npc Definitions not loaded.")
    }

    /**
     * Gets the overlay definitions.
     */
    fun overlays(): List<OverlayDefinition> {
        return definitions[OverlayDefinition::class.java]?.filterIsInstance<OverlayDefinition>()?: error("Overlay Definitions not loaded.")
    }

    /**
     * Gets the sequences definitions.
     */
    fun sequences(): List<SequenceDefinition> {
        return definitions[SequenceDefinition::class.java]?.filterIsInstance<SequenceDefinition>()?: error("Sequence Definitions not loaded.")
    }

    /**
     * Gets the spotAnim definitions.
     */
    fun spotanimations(): List<SpotAnimationDefinition> {
        return definitions[SpotAnimationDefinition::class.java]?.filterIsInstance<SpotAnimationDefinition>()?: error("Spot Animation Definitions not loaded.")
    }

    /**
     * Gets the underlay definitions.
     */
    fun underlays(): List<UnderlayDefinition> {
        return definitions[UnderlayDefinition::class.java]?.filterIsInstance<UnderlayDefinition>()?: error("Underlay Definitions not loaded.")
    }

    /**
     * Gets the varbit definitions.
     */
    fun varbits(): List<VarbitDefinition> {
        return definitions[VarbitDefinition::class.java]?.filterIsInstance<VarbitDefinition>()?: error("Varbit Definitions not loaded.")
    }

}

fun main(args : Array<String>) {

    val parser = ArgParser("app")
    val rev by parser.option(ArgType.Int, description = "The revision you wish to dump").default(0)
    parser.parse(args)

    Application.initialize(rev)
}