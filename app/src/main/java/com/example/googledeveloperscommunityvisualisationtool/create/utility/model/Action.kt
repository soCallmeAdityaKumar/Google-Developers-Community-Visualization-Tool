package com.example.googledeveloperscommunityvisualisationtool.create.utility.model

abstract class Action {
    var id: Long = 0
    var type: Int

    constructor(type: Int) {
        this.type = type
    }

    constructor(id: Long, type: Int) {
        this.id = id
        this.type = type
    }
}