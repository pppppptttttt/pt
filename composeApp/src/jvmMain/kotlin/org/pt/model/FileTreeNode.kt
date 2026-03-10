package org.pt.model

sealed interface FileTreeNode {
    val name: String
    val path: String
}

data class DirectoryNode(
    override val name: String,
    override val path: String,
    val children: MutableList<FileTreeNode> = mutableListOf()
) : FileTreeNode

data class FileNode(
    override val name: String,
    override val path: String
) : FileTreeNode

fun buildFileTree(paths: List<String>): DirectoryNode {
    val root = DirectoryNode(name = "", path = "")

    paths.sorted().forEach { fullPath ->
        val parts = fullPath.split("/").filter { it.isNotEmpty() }
        var current = root
        var currentPath = ""

        parts.forEachIndexed { index, part ->
            currentPath = if (currentPath.isEmpty()) part else "$currentPath/$part"
            val isLast = index == parts.lastIndex

            if (isLast) {
                current.children.add(
                    FileNode(
                        name = part,
                        path = currentPath
                    )
                )
            } else {
                val existing = current.children
                    .filterIsInstance<DirectoryNode>()
                    .find { it.name == part }

                val directory = existing ?: DirectoryNode(
                    name = part,
                    path = currentPath
                ).also { current.children.add(it) }

                current = directory
            }
        }
    }

    sortTree(root)
    return root
}

private fun sortTree(directory: DirectoryNode) {
    directory.children.sortWith(
        compareBy<FileTreeNode>(
            { it is FileNode },
            { it.name.lowercase() }
        )
    )

    directory.children
        .filterIsInstance<DirectoryNode>()
        .forEach(::sortTree)
}