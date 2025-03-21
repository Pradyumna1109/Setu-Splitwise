package com.example.setu_spliwise.finders;

import com.example.setu_spliwise.exceptions.NotFoundException;
import com.example.setu_spliwise.models.BaseModel;
import io.ebean.Finder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.Getter;

@Getter
public class BaseFinder<T extends BaseModel> extends Finder<UUID, T> {
  private final Class<T> modelClass;

  public BaseFinder(Class<T> modelClass) {
    super(modelClass);
    this.modelClass = modelClass;
  }

  public T get(UUID id) {
    return getNoThrow(id)
        .orElseThrow(
            () ->
                new NotFoundException(
                    String.format("Cannot find %s with ID '%s", getDisplayName(), id)));
  }

  public String getDisplayName() {
    return this.modelClass.getSimpleName().toLowerCase();
  }

  public Optional<T> getNoThrow(UUID id) {
    return query().where().idEq(id).findOneOrEmpty();
  }

  public List<T> findAll() {
    return query().where().findList();
  }

  public List<T> findAllByIds(List<UUID> ids) {
    return query().where().idIn(ids).findList();
  }
}
