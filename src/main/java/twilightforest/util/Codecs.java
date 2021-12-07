package twilightforest.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Codecs {
    public static final Codec<BlockPos> STRING_POS = Codec.STRING.comapFlatMap(Codecs::parseString2BlockPos, Vec3i::toShortString);
    public static final Codec<Direction> ONLY_HORIZONTAL = Direction.CODEC.comapFlatMap(direction -> direction.getAxis() != Direction.Axis.Y ? DataResult.success(direction) : DataResult.error("Horizontal direction only!", direction), Function.identity());

    public static final Codec<Climate.ParameterList<Supplier<Biome>>> CLIMATE_SYSTEM = ExtraCodecs.nonEmptyList(RecordCodecBuilder.<Pair<Climate.ParameterPoint, Supplier<Biome>>>create((instance) -> instance.group(Climate.ParameterPoint.CODEC.fieldOf("parameters").forGetter(Pair::getFirst), Biome.CODEC.fieldOf("biome").forGetter(Pair::getSecond)).apply(instance, Pair::of)).listOf()).xmap(Climate.ParameterList::new, Climate.ParameterList::values);

    private static DataResult<BlockPos> parseString2BlockPos(String string) {
        try {
            return Util.fixedSize(Arrays.stream(string.split(" *, *")).mapToInt(Integer::parseInt), 3).map(arr -> new BlockPos(arr[0], arr[1], arr[2]));
        } catch (Throwable e) {
            return DataResult.error(e.getMessage());
        }
    }

    private Codecs() {
    }
}
