package de.danielr1996.banking.domain.repository;

import de.danielr1996.banking.domain.entities.Saldo;
import de.danielr1996.banking.domain.repository.SaldoRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;

public class MockSaldoRepository implements SaldoRepository {
  private List<Saldo> saldos = new ArrayList<>();

  @Override
  public List<Saldo> findAll() {
    return saldos;
  }

  @Override
  public List<Saldo> findAll(Sort sort) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<Saldo> findAllById(Iterable<UUID> iterable) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends Saldo> List<S> saveAll(Iterable<S> iterable) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void flush() {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends Saldo> S saveAndFlush(S s) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteInBatch(Iterable<Saldo> iterable) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteAllInBatch() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Saldo getOne(UUID uuid) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends Saldo> List<S> findAll(Example<S> example) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends Saldo> List<S> findAll(Example<S> example, Sort sort) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Page<Saldo> findAll(Pageable pageable) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends Saldo> S save(S entity) {
    this.saldos.add(entity);
    return entity;
  }

  @Override
  public Optional<Saldo> findById(UUID uuid) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean existsById(UUID uuid) {
    throw new UnsupportedOperationException();
  }

  @Override
  public long count() {
    return saldos.size();
  }

  @Override
  public void deleteById(UUID uuid) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void delete(Saldo entity) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteAll(Iterable<? extends Saldo> entities) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteAll() {
    this.saldos.clear();
  }

  @Override
  public <S extends Saldo> Optional<S> findOne(Example<S> example) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends Saldo> Page<S> findAll(Example<S> example, Pageable pageable) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends Saldo> long count(Example<S> example) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends Saldo> boolean exists(Example<S> example) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Saldo findByKontoId(UUID kontoId) {
    throw new UnsupportedOperationException("MockSaldoRepository.findByKontoId");
  }
};
