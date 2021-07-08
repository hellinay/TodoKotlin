package com.helin.noteapplicationkotlin

import java.io.Serializable

class Person : Serializable {
    var id = 0
    var name: String? = null
    var email: String? = null
    var password: String? = null
    var surname: String? = null

    constructor( name: String?, email: String?, password: String?, surname: String?) {

        this.name = name
        this.email = email
        this.password = password
        this.surname = surname
    }
    constructor( email: String?, password: String?) {

        this.email = email
        this.password = password

    }


    override fun toString(): String {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", surname='" + surname + '\'' +
                '}'
    }
}