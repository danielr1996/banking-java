package de.danielr1996.banking.domain;

import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.repository.BuchungRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MockBuchungRepository implements BuchungRepository {
  private List<Buchung> saldos = new ArrayList<>();

  @Override
  public List<Buchung> findAll() {
    return saldos;
  }

  @Override
  public List<Buchung> findAll(Sort sort) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<Buchung> findAllById(Iterable<String> iterable) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends Buchung> List<S> saveAll(Iterable<S> iterable) {
    List<S> buchungen = StreamSupport.stream(iterable.spliterator(), false)
      .collect(Collectors.toList());
    this.saldos.addAll(buchungen);
    return new ArrayList<>();
  }

  @Override
  public void flush() {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends Buchung> S saveAndFlush(S s) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteInBatch(Iterable<Buchung> iterable) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteAllInBatch() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Buchung getOne(String uuid) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends Buchung> List<S> findAll(Example<S> example) {
//    return this.saldos.stream().filter(buchung->buchung.equals(example.getProbe())).collect(Collectors.toList());
    return new ArrayList<>();
  }

  @Override
  public <S extends Buchung> List<S> findAll(Example<S> example, Sort sort) {
    return new ArrayList<>();
  }

  @Override
  public Page<Buchung> findAll(Pageable pageable) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends Buchung> S save(S entity) {
    this.saldos.add(entity);
    return entity;
  }

  @Override
  public Optional<Buchung> findById(String uuid) {
    return Optional.empty();
  }

  @Override
  public boolean existsById(String uuid) {
    throw new UnsupportedOperationException();
  }

  @Override
  public long count() {
    return saldos.size();
  }

  @Override
  public void deleteById(String uuid) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void delete(Buchung entity) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteAll(Iterable<? extends Buchung> entities) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteAll() {
    this.saldos.clear();
  }

  @Override
  public <S extends Buchung> Optional<S> findOne(Example<S> example) {
    return Optional.empty();
  }

  @Override
  public <S extends Buchung> Page<S> findAll(Example<S> example, Pageable pageable) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends Buchung> long count(Example<S> example) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends Buchung> boolean exists(Example<S> example) {
    throw new UnsupportedOperationException();
  }
};
