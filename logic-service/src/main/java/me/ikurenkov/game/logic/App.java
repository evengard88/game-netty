import io.quarkus.runtime.Quarkus;
import me.ikurenkov.game.logic.QuarkusStart;

void main(String... args) {
    Quarkus.run(QuarkusStart.class, args);
}