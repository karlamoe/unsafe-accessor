package moe.karla.usf.unsafe.sunlegacy;

import com.google.auto.service.AutoService;
import moe.karla.usf.unsafe.Unsafe;
import moe.karla.usf.unsafe.UnsafeInitializer;
import moe.karla.usf.unsafe.UnsafeProvider;

@AutoService(UnsafeProvider.class)
public class UnsafeJdk8Provider implements UnsafeProvider {
    @Override
    public Unsafe initialize() throws Throwable {
        UnsafeInitializer.validate();
        return new Unsafe_Jdk8();
    }

    @Override
    public int priority() {
        return 0;
    }
}
