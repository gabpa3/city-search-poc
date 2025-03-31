package com.gabcode.citysearchpoc.data

import com.gabcode.citysearchpoc.domain.City

class CityTrieNode {
    val children = mutableMapOf<Char, CityTrieNode>()
    var isEndOfWord = false
    var city: City? = null
}

class CityTrie {

    private val root = CityTrieNode()

    fun insert(city: City) {
        var current = root
        val key = city.name.lowercase()

        key.forEach { char ->
            current = current.children.getOrPut(char) { CityTrieNode() }
        }

        current.isEndOfWord = true
        current.city = city
    }

    fun search(prefix: String): List<City> {
        var current = root
        val key = prefix.lowercase()
        key.forEach { char ->
            current = current.children[char] ?: return emptyList()
        }

        return collectionAllMatch(current)
    }

    private fun collectionAllMatch(
        node: CityTrieNode,
        result: MutableList<City> = mutableListOf(),
    ): List<City> {
        if (node.isEndOfWord && node.city != null) {
            result.add(node.city!!)
        }

        node.children.values.forEach { child ->
            collectionAllMatch(child, result)
        }

        return result
    }
}
