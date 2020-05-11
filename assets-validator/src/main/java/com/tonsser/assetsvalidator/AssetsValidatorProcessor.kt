package com.tonsser.assetsvalidator

import io.github.wax911.library.annotation.GraphQuery
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic


class AssetsValidatorProcessor : AbstractProcessor() {

	companion object {
		const val GRAPHQL_ASSET_DIR = "graphql"
		const val GRAPHQL_EXTENSION = "graphql"

		private val annotation = GraphQuery::class.java
	}

	override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {

		val gqlFileNames = mutableListOf<String>()
		for (element in roundEnv.getElementsAnnotatedWith(annotation)) {
			val a = element.getAnnotation(annotation)
			if (!a.ignore) {
				gqlFileNames += a.value
			}
		}

		if (gqlFileNames.isEmpty()) {
			return true
		}

		val projectDir = File(".")

		val missingFiles = checkAssetsForMissingGqlFiles(projectDir, gqlFileNames)

		if (missingFiles.isNotEmpty()) {
			processingEnv.messager.printMessage(
					Diagnostic.Kind.ERROR,
					"GraphQL files missing: $missingFiles"
			)
		}

		return true
	}

	fun checkAssetsForMissingGqlFiles(projectDir : File, gqlFileNames: List<String>) : List<String> {
		val gqlAssetsList = mutableListOf<File>()
		projectDir.listFiles()?.forEach { listFile ->
			if (listFile.isFile) {
				return@forEach
			}
			if (listFile.name.startsWith(".")) {
				return@forEach
			}

			val src = File("${listFile.path}/src")

			if (!src.exists()) {
				return@forEach
			}

			val assetsList = mutableListOf<File>()
			for (file in src.walk().maxDepth(1)) {
				val assets = File("${file.path}/assets/")
				if (assets.exists()) {
					assetsList += assets
				}
			}

			for (assets in assetsList) {
				val gqlAssets = File("${assets.path}/$GRAPHQL_ASSET_DIR")
				if (gqlAssets.exists()) {
					gqlAssetsList += gqlAssets
				}
			}

		}

		val gqlFilesMap = HashMap<String, MutableList<File>>()
		for (assetDir in gqlAssetsList) {
			assetDir.walk()
					.filter { it.isFile && it.extension == GRAPHQL_EXTENSION }
					.groupByTo(gqlFilesMap) {
						it.nameWithoutExtension
					}
		}

		val missingFiles = mutableListOf<String>()
		for (gqlFileName in gqlFileNames) {
			if (!gqlFilesMap.containsKey(gqlFileName)) {
				missingFiles += gqlFileName
			}
		}

		return missingFiles
	}

	override fun getSupportedAnnotationTypes(): Set<String> = setOf(annotation.canonicalName)

	override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()
}
