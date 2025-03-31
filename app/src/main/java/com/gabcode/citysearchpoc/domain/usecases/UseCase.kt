package com.gabcode.citysearchpoc.domain.usecases

interface UseCase<in P, out R: Any> {
    suspend fun invoke(params: P): R
}

class None
