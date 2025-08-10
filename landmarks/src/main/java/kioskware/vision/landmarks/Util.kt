package kioskware.vision.landmarks

import kioskware.vision.landmarks.`object`.Object
import kioskware.vision.landmarks.`object`.ObjectParam

@JvmName("getLandmarkByType")
fun <T : Landmark> List<T>.getByType(typeId: String): List<T> {
    return this.filter { it.typeId == typeId }
}

@JvmName("getLandmarkByTypePrefix")
fun <T : Landmark> List<T>.getByTypePrefix(typeIdPrefix: String): List<T> {
    return this.filter { it.typeId.startsWith(typeIdPrefix) }
}

@JvmName("getLandmarkByTrackingId")
fun <T : Object> List<T>.getByType(typeId: String): List<T> {
    return this.filter { it.typeId == typeId }
}

@JvmName("getObjectByTypePrefix")
fun <T : Object> List<T>.getByTypePrefix(typeIdPrefix: String): List<T> {
    return this.filter { it.typeId.startsWith(typeIdPrefix) }
}

@JvmName("getObjectByTrackingId")
fun <T : Object> List<T>.getByTrackingId(trackingId: String): T? {
    return this.firstOrNull { it.trackingId == trackingId }
}

@JvmName("getObjectParamByType")
fun List<ObjectParam<*>>.getByType(typeId: String): ObjectParam<*>? {
    return this.firstOrNull { it.typeId == typeId }
}

@JvmName("getObjectParamByTypePrefix")
fun List<ObjectParam<*>>.getByTypePrefix(typeIdPrefix: String): List<ObjectParam<*>> {
    return this.filter { it.typeId.startsWith(typeIdPrefix) }
}