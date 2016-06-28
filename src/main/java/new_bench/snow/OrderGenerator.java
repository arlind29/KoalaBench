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
import new_bench.util.GenerateUtils;
import new_bench.util.RandomBoundedInt;
import new_bench.util.RandomBoundedLong;
import new_bench.util.RandomString;
import new_bench.util.RandomText;
import new_bench.util.TextPool;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Locale.ENGLISH;
import static new_bench.snow.LineItemGenerator.ITEM_SHIP_DAYS;
import static new_bench.snow.LineItemGenerator.createDiscountRandom;
import static new_bench.snow.LineItemGenerator.createPartKeyRandom;
import static new_bench.snow.LineItemGenerator.createQuantityRandom;
import static new_bench.snow.LineItemGenerator.createShipDateRandom;
import static new_bench.snow.LineItemGenerator.createTaxRandom;
import static new_bench.snow.PartGenerator.calculatePartPrice;
import static new_bench.util.GenerateUtils.MIN_GENERATE_DATE;
import static new_bench.util.GenerateUtils.TOTAL_DATE_RANGE;
import static new_bench.util.GenerateUtils.calculateRowCount;
import static new_bench.util.GenerateUtils.calculateStartIndex;
import static new_bench.util.GenerateUtils.toEpochDate;

public class OrderGenerator
        implements Iterable<Order>
{
    public static final int SCALE_BASE = 1_500_000;

    // portion with have no orders
    public static final int CUSTOMER_MORTALITY = 3;

    private static final int ORDER_DATE_MIN = MIN_GENERATE_DATE;
    private static final int ORDER_DATE_MAX = ORDER_DATE_MIN + (TOTAL_DATE_RANGE - ITEM_SHIP_DAYS - 1);
    private static final int CLERK_SCALE_BASE = 1000;

    private static final int LINE_COUNT_MIN = 1;
    static final int LINE_COUNT_MAX = 7;

    private static final int COMMENT_AVERAGE_LENGTH = 49;

    private static final int ORDER_KEY_SPARSE_BITS = 2;
    private static final int ORDER_KEY_SPARSE_KEEP = 3;

    private final double scaleFactor;
    private final int part;
    private final int partCount;

    private final Distributions distributions;
    private final TextPool textPool;

    public OrderGenerator(double scaleFactor, int part, int partCount)
    {
        this(scaleFactor, part, partCount, Distributions.getDefaultDistributions(), TextPool.getDefaultTestPool());
    }

    public OrderGenerator(double scaleFactor, int part, int partCount, Distributions distributions, TextPool textPool)
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
    public Iterator<Order> iterator()
    {
        return new OrderGeneratorIterator(
                distributions,
                textPool,
                scaleFactor,
                calculateStartIndex(SCALE_BASE, scaleFactor, part, partCount),
                calculateRowCount(SCALE_BASE, scaleFactor, part, partCount));
    }

    public static class OrderGeneratorIterator
            extends AbstractIterator<Order>
    {
        private final RandomBoundedInt orderDateRandom = createOrderDateRandom();
        private final RandomBoundedInt lineCountRandom = createLineCountRandom();
        private final RandomBoundedLong customerKeyRandom;
        private final RandomString orderPriorityRandom;
        private final RandomBoundedInt clerkRandom;
        private final RandomText commentRandom;

        private final RandomBoundedInt lineQuantityRandom = createQuantityRandom();
        private final RandomBoundedInt lineDiscountRandom = createDiscountRandom();
        private final RandomBoundedInt lineTaxRandom = createTaxRandom();
        private final RandomBoundedLong linePartKeyRandom;
        private final RandomBoundedInt lineShipDateRandom = createShipDateRandom();

        private final long startIndex;
        private final long rowCount;

        private final long maxCustomerKey;

        private long index;

        public OrderGeneratorIterator(Distributions distributions, TextPool textPool, double scaleFactor, long startIndex, long rowCount)
        {
            this.startIndex = startIndex;
            this.rowCount = rowCount;

            clerkRandom = new RandomBoundedInt( 1, Math.max((int) (scaleFactor * CLERK_SCALE_BASE), CLERK_SCALE_BASE));

            maxCustomerKey = (long) (CustomerGenerator.SCALE_BASE * scaleFactor);
            customerKeyRandom = new RandomBoundedLong( scaleFactor >= 30000, 1, maxCustomerKey);

            orderPriorityRandom = new RandomString( distributions.getOrderPriorities());
            commentRandom = new RandomText( textPool, COMMENT_AVERAGE_LENGTH);

            linePartKeyRandom = createPartKeyRandom(scaleFactor);
        }

        @Override
        public Order computeNext()
        {
            if (index >= rowCount) {
                return endOfData();
            }

            Order order = makeOrder(startIndex + index + 1);
            index++;
            return order;
        }

        public Order makeOrder(long index)
        {
            long orderKey = makeOrderKey(index);

            int orderDate = orderDateRandom.getValue(orderKey);

            // generate customer key, taking into account customer mortality rate
            long customerKey = customerKeyRandom.getValue(orderKey);
            int delta = 1;
            while (customerKey % CUSTOMER_MORTALITY == 0) {
                customerKey += delta;
                customerKey = Math.min(customerKey, maxCustomerKey);
                delta *= -1;
            }


            long totalPrice = 0;
            int shippedCount = 0;

            int lineCount = lineCountRandom.getValue(orderKey);
            for (long lineNumber = 0; lineNumber < lineCount; lineNumber++) {
                int quantity = lineQuantityRandom.getValue(orderKey);
                int discount = lineDiscountRandom.getValue(orderKey);
                int tax = lineTaxRandom.getValue(orderKey);

                long partKey = linePartKeyRandom.getValue(orderKey);

                long partPrice = calculatePartPrice(partKey);
                long extendedPrice = partPrice * quantity;
                long discountedPrice = extendedPrice * (100 - discount);
                totalPrice += ((discountedPrice / 100) * (100 + tax)) / 100;

                int shipDate = lineShipDateRandom.getValue(orderKey);
                shipDate += orderDate;
                if (GenerateUtils.isInPast(shipDate)) {
                    shippedCount++;
                }
            }

            char orderStatus;
            if (shippedCount == lineCount) {
                orderStatus = 'F';
            }
            else if (shippedCount > 0) {
                orderStatus = 'P';
            }
            else {
                orderStatus = 'O';
            }

            return new Order(
                    index,
                    orderKey,
                    customerKey,
                    orderStatus,
                    totalPrice,
                    toEpochDate(orderDate),
                    orderPriorityRandom.getValue(orderKey),
                    String.format(ENGLISH, "Clerk#%09d", clerkRandom.getValue(orderKey)),
                    0,
                    commentRandom.getValue(orderKey));
        }
    }

    static RandomBoundedInt createLineCountRandom()
    {
        return new RandomBoundedInt( LINE_COUNT_MIN, LINE_COUNT_MAX);
    }

    static RandomBoundedInt createOrderDateRandom()
    {
        return new RandomBoundedInt( ORDER_DATE_MIN, ORDER_DATE_MAX);
    }

    static long makeOrderKey(long orderIndex)
    {
        long low_bits = orderIndex & ((1 << ORDER_KEY_SPARSE_KEEP) - 1);

        long ok = orderIndex;
        ok = ok >> ORDER_KEY_SPARSE_KEEP;
        ok = ok << ORDER_KEY_SPARSE_BITS;
        ok = ok << ORDER_KEY_SPARSE_KEEP;
        ok += low_bits;

        return ok;
    }
}
