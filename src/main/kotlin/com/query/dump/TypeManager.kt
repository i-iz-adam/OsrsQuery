package com.query.dump

import com.query.cache.definitions.impl.*
import com.query.cache.download.CacheLoader

interface TypeManager {

    fun test() {
        CacheLoader.initialize()
        requiredDefs.forEach {
            runType(it)
        }
        onTest()
    }

    fun runType(type : DefinitionsTypes) {
        when(type) {
            DefinitionsTypes.SPRITES -> SpriteProvider(null,false).run()
            DefinitionsTypes.AREAS -> AreaProvider(null,false).run()
            DefinitionsTypes.OBJECTS -> ObjectProvider(null,false).run()
            DefinitionsTypes.ITEMS -> ItemProvider(null,false).run()
            DefinitionsTypes.TEXTURES -> TextureProvider(null,false).run()
            DefinitionsTypes.OVERLAYS -> OverlayProvider(null,false).run()
        }
    }

    open val requiredDefs : List<DefinitionsTypes>

    abstract fun load()

    abstract fun onTest()


}