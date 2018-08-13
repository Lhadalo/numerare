package com.lhadalo.oladahl.numerare.domain

import com.lhadalo.oladahl.numerare.data.CounterEntity
import com.lhadalo.oladahl.numerare.presentation.model.CounterModel
import javax.inject.Inject

class CounterUseCase @Inject constructor(private val repository: CounterRepository) : CounterModel {

    override fun getAll() = repository.getAll()

    override fun get(id: Int) = repository.get(id)

    override fun add(counter: CounterEntity) = repository.add(counter)

    override fun update(counter: CounterEntity) = repository.update(counter)

    override fun updateCount(id: Int, newValue: Int) = repository.updateCount(id, newValue)

    override fun delete(counter: CounterEntity) = repository.delete(counter)
}