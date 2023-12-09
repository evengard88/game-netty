package com.ikurenkov.game.configuration;

import java.io.Serializable;

public record Pair<T, T1>(T t,
                          T1 t1)  implements Serializable {

}
