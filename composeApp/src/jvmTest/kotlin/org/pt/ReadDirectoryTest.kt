package org.pt

//import org.pt.model.readDirectory
//import java.nio.file.Files
//import java.nio.file.Path
//import kotlin.io.path.createDirectories
//import kotlin.io.path.writeText
//import kotlin.test.AfterTest
//import kotlin.test.Test
//import kotlin.test.assertEquals
//import kotlin.test.assertFailsWith
//import kotlin.test.assertTrue
//
//class ReadDirectoryTest {
//
//    private val tempPaths = mutableListOf<Path>()
//
//    @AfterTest
//    fun cleanup() {
//        tempPaths.asReversed().forEach { path ->
//            Files.walk(path)
//                .sorted(Comparator.reverseOrder())
//                .forEach(Files::deleteIfExists)
//        }
//        tempPaths.clear()
//    }
//
//    private fun tempDir(): Path {
//        val dir = Files.createTempDirectory("read-directory-test-")
//        tempPaths.add(dir)
//        return dir
//    }
//
//    @Test
//    fun `returns empty string for empty directory`() {
//        val dir = tempDir()
//
//        val result = readDirectory(dir)
//
//        assertEquals("", result)
//    }
//
//    @Test
//    fun `reads content from files in directory`() {
//        val dir = tempDir()
//        dir.resolve("a.txt").writeText("hello")
//        dir.resolve("b.txt").writeText("world")
//
//        val result = readDirectory(dir)
//
//        assertTrue(result.contains("hello"))
//        assertTrue(result.contains("world"))
//    }
//
//    @Test
//    fun `reads content from nested directories`() {
//        val dir = tempDir()
//        val nested = dir.resolve("nested").createDirectories()
//        nested.resolve("inner.txt").writeText("inner content")
//
//        val result = readDirectory(dir)
//
//        assertTrue(result.contains("inner content"))
//    }
//
//    @Test
//    fun `throws when directory does not exist`() {
//        val dir = tempDir()
//        val missing = dir.resolve("missing")
//
//        assertFailsWith<Exception> {
//            readDirectory(missing)
//        }
//    }
//
//    @Test
//    fun `throws when path is a file`() {
//        val dir = tempDir()
//        val file = dir.resolve("file.txt")
//        file.writeText("not a directory")
//
//        assertFailsWith<Exception> {
//            readDirectory(file)
//        }
//    }
//
//    @Test
//    fun `uses empty prefix by default`() {
//        val dir = tempDir()
//        dir.resolve("a.txt").writeText("hello")
//
//        val result = readDirectory(dir)
//
//        assertEquals("hello\n", result)
//    }
//
//    @Test
//    fun `adds prefix for each file`() {
//        val dir = tempDir()
//        dir.resolve("a.txt").writeText("hello")
//        dir.resolve("b.txt").writeText("world")
//
//        val result = readDirectory(dir) { file -> "[${file.name}] " }
//
//        assertEquals("[a.txt] hello\n[b.txt] world\n", result)
//    }
//
//    @Test
//    fun `passes actual file to prefix function`() {
//        val dir = tempDir()
//        val nested = dir.resolve("nested").createDirectories()
//        nested.resolve("inner.txt").writeText("content")
//
//        val result = readDirectory(dir) { file -> "${file.parentFile.name}/${file.name}: " }
//
//        assertEquals("nested/inner.txt: content\n", result)
//    }
//
//    @Test
//    fun `prefix function is called for every file`() {
//        val dir = tempDir()
//        dir.resolve("a.txt").writeText("one")
//        dir.resolve("b.txt").writeText("two")
//        dir.resolve("c.txt").writeText("three")
//
//        val seenFiles = mutableListOf<String>()
//
//        val result = readDirectory(dir) { file ->
//            println("visit ${file.name}")
//            seenFiles += file.name
//            ""
//        }
//
//        assertEquals(listOf("a.txt", "b.txt", "c.txt"), seenFiles.sorted())
//        assertEquals("one\ntwo\nthree\n", result)
//    }
//
//    @Test
//    fun `supports prefix based on file content or metadata`() {
//        val dir = tempDir()
//        dir.resolve("a.txt").writeText("abc")
//        dir.resolve("b.txt").writeText("hello")
//
//        val result = readDirectory(dir) { file -> "${file.length()}: " }
//
//        assertEquals("3: abc\n5: hello\n", result)
//    }
//}
