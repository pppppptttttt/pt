package org.pt.model

import io.github.vinceglb.filekit.utils.toFile
import org.eclipse.jgit.ignore.IgnoreNode
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.concurrent.ConcurrentHashMap
import kotlinx.io.files.Path

data class IgnoreConfiguration(val considerGitIgnore: Boolean = true, val excludeDotGitIgnore: Boolean = true)

class DirectoryReader(
    private val baseDirectory: Path,
    private val ignoreConfiguration: IgnoreConfiguration = IgnoreConfiguration()
) {
    companion object {
        private const val BINARY_CHECK_BYTES = 8192
    }

    init {
        if (!baseDirectory.toFile().exists()) {
            throw FileNotFoundException("Directory $baseDirectory does not exist")
        }
        if (!baseDirectory.toFile().isDirectory) {
            throw IllegalArgumentException("$baseDirectory is not a directory")
        }
    }

    val files = SelectedFiles()
    private val ignoreCache = ConcurrentHashMap<File, IgnoreNode?>()

    fun readDirectory(
        prefixFunc: (File) -> String = { "" },
        postfixFunc: (File) -> String = { "" },
        binaryFunc: (File) -> String = { it.readText() },
        selectBinaries: Boolean = false,
    ) {
        baseDirectory.toFile().walk()
            .onEnter { dir ->
                dir == baseDirectory.toFile() || !isIgnored(dir)
            }
            .toList()
            .parallelStream()
            .filter { it.isFile && !isIgnored(it) }
            .forEach {
                val binary = it.looksBinary()
                val content = if (binary) binaryFunc(it) else it.readText()

                files.put(
                    it.relativeTo(baseDirectory.toFile()).path,
                    "${prefixFunc(it)}$content\n${postfixFunc(it)}",
                    selected = if (it.looksBinary()) selectBinaries else true
                )
            }
    }

    private fun File.looksBinary(): Boolean {
        FileInputStream(this).use { input ->
            val buffer = ByteArray(BINARY_CHECK_BYTES)
            val read = input.read(buffer)
            if (read <= 0) return false
            return buffer.take(read).any { it == 0.toByte() }
        }
    }

    private fun isIgnored(file: File): Boolean {
        val baseDirFile = baseDirectory.toFile().canonicalFile
        val canonicalFile = file.canonicalFile

        if (canonicalFile == baseDirFile) return false
        if (canonicalFile.name == ".git") return ignoreConfiguration.excludeDotGitIgnore
        if (!ignoreConfiguration.considerGitIgnore) return false

        val parents = directoriesFromBaseToCurrent(canonicalFile)

        var result = IgnoreNode.MatchResult.CHECK_PARENT

        for (dir in parents) {
            val node = loadGitIgnore(dir) ?: continue

            val relativePath = relativePathForIgnoreRuleScope(canonicalFile, dir)
            if (relativePath.isEmpty()) continue

            val match = node.isIgnored(relativePath, canonicalFile.isDirectory)
            if (match != IgnoreNode.MatchResult.CHECK_PARENT) {
                result = match
            }
        }

        return result == IgnoreNode.MatchResult.IGNORED
    }

    private fun directoriesFromBaseToCurrent(file: File): List<File> {
        val baseDirFile = baseDirectory.toFile().canonicalFile
        val startDir = if (file.isDirectory) file.parentFile?.canonicalFile else file.parentFile?.canonicalFile

        val dirs = mutableListOf<File>()
        var current = startDir

        while (current != null) {
            if (current == baseDirFile || current.toPath().startsWith(baseDirFile.toPath())) {
                dirs += current
            } else {
                break
            }

            if (current == baseDirFile) break
            current = current.parentFile?.canonicalFile
        }

        return dirs.asReversed()
    }

    private fun relativePathForIgnoreRuleScope(target: File, gitIgnoreDir: File): String {
        val path = target.relativeTo(gitIgnoreDir).invariantSeparatorsPath
        return if (target.isDirectory && path.isNotEmpty()) "$path/" else path
    }

    private fun loadGitIgnore(dir: File): IgnoreNode? {
        return ignoreCache.computeIfAbsent(dir) {
            val gitIgnoreFile = dir.resolve(".gitignore")
            if (!gitIgnoreFile.exists() || !gitIgnoreFile.isFile) {
                null
            } else {
                FileInputStream(gitIgnoreFile).use { input ->
                    IgnoreNode().apply { parse(input) }
                }
            }
        }
    }
}
