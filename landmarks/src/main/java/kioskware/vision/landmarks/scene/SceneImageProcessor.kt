package kioskware.vision.landmarks.scene

import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.Image
import kioskware.vision.landmarks.Landmark
import kioskware.vision.landmarks.`object`.Object
import kioskware.vision.landmarks.`object`.ObjectParam
import kioskware.vision.processor.ImageProcessor
import kioskware.vision.processor.ProcessingResults

/**
 * Abstract class for processing images to detect landmarks and objects.
 * This class extends the ImageProcessor with a generic type of Scene.
 */
abstract class SceneImageProcessor : ImageProcessor<Scene?>() {

    companion object {

        /**
         * Composes a Scene from the provided ProcessingResults.
         *
         * This function aggregates all Scene objects found in the processing results
         * and combines them into a single Scene object.
         *
         * @param processingResults The ProcessingResults containing multiple Scene objects.
         * @return A single Scene object composed from the processing results, or null if no Scene objects are found.
         */
        fun composeSceneFromProcessingResults(
            processingResults: ProcessingResults
        ): Scene? {
            var cScene: Scene? = null
            processingResults.getAllValues()
                .filterIsInstance<Scene>()
                .forEach {
                    if (cScene == null) {
                        cScene = it
                    } else {
                        cScene += it
                    }
                }
            return cScene
        }

    }

    /**
     * Processes the given image to detect landmarks and objects.
     *
     * @param image The image bitmap to process.
     *
     * Should return result scene using [createResultScene] method.
     *
     * Example:
     * ```
     * override suspend fun onProcess(
     *     image: Bitmap,
     * ): Scene {
     *     // Perform detection logic here
     *     val landmarks = detectLandmarks(image, rotationDegrees)
     *     val objects = detectObjects(image, rotationDegrees)
     *     val params = detectParams(image, rotationDegrees)
     *     return createResultScene(
     *        image = image,
     *        rotationDegrees = rotationDegrees,
     *        landmarks = landmarks,
     *        objects = objects,
     *        params = params
     *     )
     * }
     * ```
     *
     * @return A Scene object containing the processed image, landmarks, and objects.
     */
    abstract suspend fun onProcess(
        image: Bitmap,
    ): Scene?

    /**
     * Renders visualization on the provided overlay canvas.
     *
     * This method is called after [onProcess] to allow rendering of additional visual elements
     * such as landmarks, bounding boxes, or other visual aids.
     *
     * @param scene The Scene object containing the processed data.
     * @param overlayCanvas The Canvas on which to render the visualization.
     */
    open suspend fun onRenderVisualization(
        scene: Scene,
        image: Bitmap,
        overlayCanvas: Canvas
    ) {
        // Default implementation does nothing
        // override to provide visualization rendering
    }

    // From ImageProcessor<T> interface
    final override suspend fun onProcess(
        image: Bitmap,
        overlayCanvas: Canvas?
    ): Scene? {
        val scene = onProcess(image)
        scene?.let { scene ->
            overlayCanvas?.let {
                onRenderVisualization(scene, image, it)
            }
        }
        return scene
    }

    /**
     * Helper function to create a Scene object with the processed image and landmarks.
     *
     * Should be used inside [onProcess] implementations to create the result scene.
     *
     * @param image The original image bitmap being processed. Just pass by the same reference from the [onProcess] method.
     * @param landmarks The list of detected landmarks. Defaults to an empty list.
     * @param objects The list of detected objects. Defaults to an empty list.
     * @param params Additional parameters for the scene. Defaults to an empty list.
     * @return A Scene object containing the processed image, landmarks, and objects.
     */
    protected fun createResultScene(
        image: Bitmap,
        landmarks: List<Landmark> = emptyList(),
        objects: List<Object> = emptyList(),
        params: List<ObjectParam<*>> = emptyList()
    ): Scene = Scene(
        originalWidth = image.width,
        originalHeight = image.height,
        landmarks = landmarks,
        objects = objects,
        params = params,
        sourceProcessor = this
    )

}