package ch.unibe.scg.nullfinder.jpa.entity.builder;

public abstract class AbstractEntityBuilder<E> implements IEntityBuilder<E> {

	protected E entity;

	public AbstractEntityBuilder(E entity) {
		this.entity = entity;
	}

	@Override
	public E getEntity() {
		return this.entity;
	}

}
