package com.kubadziworski.domain.types;

import com.kubadziworski.domain.types.builder.MemberBuilder;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by pablo.lozano on 4/11/2017.
 */
public class TypeParameterImpl implements TypeParameter {

    private final String simpleName;
    private final List<Constraint> constraints;

    @Builder
    public TypeParameterImpl(String simpleName, List<MemberBuilder<Constraint, TypeParameter>> constraints) {
        this.simpleName = simpleName;
        this.constraints = constraints.stream()
                .map(constraintMemberBuilder -> constraintMemberBuilder.build(this))
                .collect(Collectors.toList());
    }

    @Override
    public String getQualifiedName() {
        return simpleName;
    }


    @Override
    public ArrayType getArrayType(){
        //TODO
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSimpleName() {
        return simpleName;
    }

    @Override
    public String getName() {
        return simpleName;
    }

    @Override
    public List<Constraint> getConstraints() {
        return constraints;
    }
}