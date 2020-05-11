package com.tonsser.assetsvalidator

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.File

class AssetsValidatorProcessorTest {

	lateinit var processor : AssetsValidatorProcessor
	lateinit var projectDir : File

	@Before
	fun setUp() {
		processor = AssetsValidatorProcessor()
		projectDir = File("../")
	}

	@Test
	fun testCheckAssetsForGqlFiles() {
		val expected1 = listOf("a")
		val missing1 = processor.checkAssetsForMissingGqlFiles(projectDir, expected1)
		Assert.assertEquals(missing1, expected1)

		val missing2 = processor.checkAssetsForMissingGqlFiles(projectDir, listOf("RepoEntries"))
		assert(missing2.isEmpty())
	}

}