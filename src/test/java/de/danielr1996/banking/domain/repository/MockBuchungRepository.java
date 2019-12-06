package de.danielr1996.banking.domain.repository;

import de.danielr1996.banking.domain.entities.Buchung;
import de.danielr1996.banking.domain.repository.BuchungRepository;
import org.springframework.data.domain.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MockBuchungRepository implements BuchungRepository {
  private List<Buchung> buchungen = new ArrayList<>();

  @Override
  public List<Buchung> findAll() {
    return buchungen;
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
    this.buchungen.addAll(buchungen);
    return buchungen;
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
//    return this.buchungen.stream().filter(buchung->buchung.equals(example.getProbe())).collect(Collectors.toList());
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends Buchung> List<S> findAll(Example<S> example, Sort sort) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Page<Buchung> findAll(Pageable pageable) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends Buchung> S save(S entity) {
    this.buchungen.add(entity);
    return entity;
  }

  @Override
  public Optional<Buchung> findById(String uuid) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean existsById(String uuid) {
    throw new UnsupportedOperationException();
  }

  @Override
  public long count() {
    return buchungen.size();
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
    this.buchungen.clear();
  }

  @Override
  public <S extends Buchung> Optional<S> findOne(Example<S> example) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends Buchung> Page<S> findAll(Example<S> example, Pageable pageable) {

    @SuppressWarnings("unchecked")
    List<S> ss = buchungen
      .stream()
      .filter(s -> s.equals(example.getProbe()))
      .map(b -> (S) b)
      .collect(Collectors.toList());
    return new PageImpl<>(ss);
  }

  @Override
  public <S extends Buchung> long count(Example<S> example) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends Buchung> boolean exists(Example<S> example) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Page<Buchung> findByKontoIdIn(Collection<UUID> kontoIds, Pageable pageable) {
    throw new UnsupportedOperationException();
  }
};
