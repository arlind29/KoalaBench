/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package new_bench.snow;

import com.google.common.collect.AbstractIterator;

import java.util.Iterator;

import new_bench.util.Distributions;
import new_bench.util.RandomBoundedInt;
import new_bench.util.RandomString;
import new_bench.util.RandomStringSequence;
import new_bench.util.RandomText;
import new_bench.util.TextPool;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Locale.ENGLISH;
import static new_bench.util.GenerateUtils.calculateRowCount;
import static new_bench.util.GenerateUtils.calculateStartIndex;

public class PartGenerator
        implements Iterable<Part>
{
    public static final int SCALE_BASE = 200_000;

    private static final int NAME_WORDS = 5;
    private static final int MANUFACTURER_MIN = 1;
    private static final int MANUFACTURER_MAX = 5;
    private static final int BRAND_MIN = 1;
    private static final int BRAND_MAX = 5;
    private static final int SIZE_MIN = 1;
    private static final int SIZE_MAX = 50;
    private static final int COMMENT_AVERAGE_LENGTH = 14;

    private final double scaleFactor;
    private final int part;
    private final int partCount;

    private final Distributions distributions;
    private final TextPool textPool;

    public PartGenerator(double scaleFactor, int part, int partCount)
    {
        this(scaleFactor, part, partCount, Distributions.getDefaultDistributions(), TextPool.getDefaultTestPool());
    }

    public PartGenerator(double scaleFactor, int part, int partCount, Distributions distributions, TextPool textPool)
    {
        checkArgument(scaleFactor > 0, "scaleFactor must be greater than 0");
        checkArgument(part >= 1, "part must be at least 1");
        checkArgument(part <= partCount, "part must be less than or equal to part count");

        this.scaleFactor = scaleFactor;
        this.part = part;
        this.partCount = partCount;

        this.distributions = checkNotNull(distributions, "distributions is null");
        this.textPool = checkNotNull(textPool, "textPool is null");
    }

    @Override
    public Iterator<Part> iterator()
    {
        return new PartGeneratorIterator(
                distributions,
                textPool,
                calculateStartIndex(SCALE_BASE, scaleFactor, part, partCount),
                calculateRowCount(SCALE_BASE, scaleFactor, part, partCount));
    }

    public static class PartGeneratorIterator
            extends AbstractIterator<Part>
    {
        private final RandomStringSequence nameRandom;
        private final RandomBoundedInt manufacturerRandom;
        private final RandomBoundedInt brandRandom;
        private final RandomString typeRandom;
        private final RandomBoundedInt sizeRandom;
        private final RandomString containerRandom;
        private final RandomText commentRandom;

        private final long startIndex;
        private final long rowCount;

        private long index;

        public PartGeneratorIterator(Distributions distributions, TextPool textPool, long startIndex, long rowCount)
        {
            this.startIndex = startIndex;
            this.rowCount = rowCount;

            nameRandom = new RandomStringSequence( NAME_WORDS, distributions.getPartColors());
            manufacturerRandom = new RandomBoundedInt( MANUFACTURER_MIN, MANUFACTURER_MAX);
            brandRandom = new RandomBoundedInt( BRAND_MIN, BRAND_MAX);
            typeRandom = new RandomString( distributions.getPartTypes());
            sizeRandom = new RandomBoundedInt(SIZE_MIN, SIZE_MAX);
            containerRandom = new RandomString(distributions.getPartContainers());
            commentRandom = new RandomText(textPool, COMMENT_AVERAGE_LENGTH);

        }

        @Override
        protected Part computeNext()
        {
            if (index >= rowCount) {
                return endOfData();
            }

            Part part = makePart(startIndex + index + 1);
            index++;
            return part;
        }

        public Part makePart(long partKey)
        {
            String name = nameRandom.getValue(partKey);

            int manufacturer = manufacturerRandom.getValue(partKey);
            int brand = manufacturer * 10 + brandRandom.getValue(partKey);

            return new Part(partKey,
                    partKey,
                    name,
                    String.format(ENGLISH, "Manufacturer#%d", manufacturer),
                    String.format(ENGLISH, "Brand#%d", brand),
                    typeRandom.getValue(partKey),
                    sizeRandom.getValue(partKey),
                    containerRandom.getValue(partKey),
                    calculatePartPrice(partKey),
                    commentRandom.getValue(partKey));
        }
    }

    static long calculatePartPrice(long p)
    {
        long price = 90000;

        // limit contribution to $200
        price += (p / 10) % 20001;
        price += (p % 1000) * 100;

        return (price);
    }
}
