package com.kubadziworski.domain.types;

import lombok.Builder;

public class UpperBoundConstraintImpl extends ConstraintImpl implements UpperBoundConstraint {

    private final TypeReference type;

    @Builder
    public UpperBoundConstraintImpl(TypeReference type, ConstraintOwner owner) {
        super(owner);
        this.type = type;

    }

    @Override
    public TypeReference getType() {
        return type;
    }

}