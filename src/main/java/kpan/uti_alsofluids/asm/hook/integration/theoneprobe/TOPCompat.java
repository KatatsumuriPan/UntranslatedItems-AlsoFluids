package kpan.uti_alsofluids.asm.hook.integration.theoneprobe;

import java.util.function.Function;

import javax.annotation.Nullable;

import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class TOPCompat {

	public static void register() {
		if (Loader.isModLoaded("theoneprobe")) {
			FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", GetTheOneProbe.class.getName());
		}
	}

	public static class GetTheOneProbe implements Function<ITheOneProbe, Void> {
		public static TheOneProbeImp probe;

		@Override
		@Nullable
		public Void apply(ITheOneProbe theOneProbe) {
			probe = (TheOneProbeImp) theOneProbe;
			ElementFluidBlockLabel.elementId = probe.registerElementFactory(ElementFluidBlockLabel::new);
			return null;
		}
	}

}
