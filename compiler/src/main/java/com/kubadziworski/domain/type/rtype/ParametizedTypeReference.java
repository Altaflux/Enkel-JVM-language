package com.kubadziworski.domain.type.rtype;

import java.util.List;

/**
 * Created by plozano on 3/29/2017.
 */
public interface ParametizedTypeReference extends TypeReference{

    //List<String this>
    List<TypeReference> getArguments();

}