import com.google.inject.Guice;
import com.google.inject.Injector;
import me.ikurenkov.game.logic.application.port.out.DisconnectPort;
import me.ikurenkov.game.logic.application.port.out.MessagePort;
import me.ikurenkov.game.logic.configuration.ApplicationModule;
import me.ikurenkov.game.logic.configuration.GameRulesModule;
import me.ikurenkov.game.logic.configuration.JsonMapperModule;
import me.ikurenkov.game.logic.configuration.LettuceModule;

import java.time.Duration;

void main(String... args) throws InterruptedException {
    Injector injector = Guice.createInjector(
            new LettuceModule(args[0], Integer.parseInt(args[1])),
            new JsonMapperModule(),
            new GameRulesModule(),
            new ApplicationModule()
    );
    while (true){}
}
