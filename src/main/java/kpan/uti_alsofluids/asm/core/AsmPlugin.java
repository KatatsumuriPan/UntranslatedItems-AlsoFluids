package kpan.uti_alsofluids.asm.core;

import java.util.Map;
import javax.annotation.Nullable;
import kpan.uti_alsofluids.ModTagsGenerated;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;

@IFMLLoadingPlugin.TransformerExclusions({ModTagsGenerated.MODGROUP + ".asm.core.", ModTagsGenerated.MODGROUP + ".asm.tf.", ModTagsGenerated.MODGROUP + ".util.MyReflectionHelper"})
@Name("AsmPlugin")
@MCVersion("1.12.2")
public class AsmPlugin implements IFMLLoadingPlugin {

    public AsmPlugin() {
        AsmUtil.LOGGER.debug("This is " + (AsmUtil.isDeobfEnvironment() ? "deobf" : "obf") + " environment");
    }

    @Override
    public String[] getASMTransformerClass() { return new String[]{ASMTransformer.class.getName()}; }

    @Override
    public String getModContainerClass() { return null; }

    @Nullable
    @Override
    public String getSetupClass() { return null; }

    @Override
    public void injectData(Map<String, Object> data) { }

    @Override
    public String getAccessTransformerClass() { return null; }

}
