package com.query.cache.map.builders

data class MapImageBuilder(
    var drawObjects : Boolean = true,
    var drawMapScene  : Boolean = true,
    var drawFunctions  : Boolean = true,
    var labelRegions  : Boolean = false,
    var outlineRegions  : Boolean = false,
    var scale : Int = 4
) {
    fun objects(state: Boolean) = apply { this.drawObjects = state }
    fun mapScenes(state: Boolean) = apply { this.drawMapScene = state }
    fun functions(state: Boolean) = apply { this.drawFunctions = state }
    fun label(state: Boolean) = apply { this.labelRegions = state }
    fun scale(scale: Int) = apply { this.scale = scale }
    fun outline(state: Boolean) = apply { this.outlineRegions = state }
    fun build() = MapImageBuilder(drawObjects, drawMapScene,drawFunctions, labelRegions,outlineRegions,scale)
}