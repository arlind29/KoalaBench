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
import static new_bench.snow.OrderGenerator.createLineCountRandom;
import static new_bench.snow.OrderGenerator.createOrderDateRandom;
import static new_bench.snow.OrderGenerator.makeOrderKey;
import static new_bench.snow.PartSupplierGenerator.selectPartSupplier;
import static new_bench.util.GenerateUtils.calculateRowCount;
import static new_bench.util.GenerateUtils.calculateStartIndex;
import static new_bench.util.GenerateUtils.toEpochDate;

public class LineItemGenerator
        implements Iterable<LineItem>
{
    private static final int QUANTITY_MIN = 1;
    private static final int QUANTITY_MAX = 50;
    private static final int TAX_MIN = 0;
    private static final int TAX_MAX = 8;
    private static final int DISCOUNT_MIN = 0;
    private static final int DISCOUNT_MAX = 10;
    private static final int PART_KEY_MIN = 1;

    private static final int SHIP_DATE_MIN = 1;
    private static final int SHIP_DATE_MAX = 121;
    private static final int COMMIT_DATE_MIN = 30;
    private static final int COMMIT_DATE_MAX = 90;
    private static final int RECEIPT_DATE_MIN = 1;
    private static final int RECEIPT_DATE_MAX = 30;

    static final int ITEM_SHIP_DAYS = SHIP_DATE_MAX + RECEIPT_DATE_MAX;

    private static final int COMMENT_AVERAGE_LENGTH = 27;

    private final double scaleFactor;
    private final int part;
    private final int partCount;
    //private final long randomNumber; 

    private final Distributions distributions;
    private final TextPool textPool;

    public LineItemGenerator(double scaleFactor, int part, int partCount)
    {
        this(scaleFactor, part, partCount, Distributions.getDefaultDistributions(), TextPool.getDefaultTestPool());
    }

    public LineItemGenerator(double scaleFactor, int part, int partCount, Distributions distributions, TextPool textPool)
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
    public Iterator<LineItem> iterator()
    {
        return new LineItemGeneratorIterator(
                distributions,
                textPool,
                scaleFactor,
                calculateStartIndex(OrderGenerator.SCALE_BASE, scaleFactor, part, partCount),
                calculateRowCount(OrderGenerator.SCALE_BASE, scaleFactor, part, partCount));
    }

    public static class LineItemGeneratorIterator
            extends AbstractIterator<LineItem>
    {
        private final RandomBoundedInt orderDateRandom = createOrderDateRandom();
        private final RandomBoundedInt lineCountRandom = createLineCountRandom();

        private final RandomBoundedInt quantityRandom = createQuantityRandom();
        private final RandomBoundedInt discountRandom = createDiscountRandom();
        private final RandomBoundedInt taxRandom = createTaxRandom();

        private final RandomBoundedLong linePartKeyRandom;

        private final RandomBoundedInt supplierNumberRandom = new RandomBoundedInt( 0, 3);

        private final RandomBoundedInt shipDateRandom = createShipDateRandom();
        private final RandomBoundedInt commitDateRandom = new RandomBoundedInt( COMMIT_DATE_MIN, COMMIT_DATE_MAX);
        private final RandomBoundedInt receiptDateRandom = new RandomBoundedInt(RECEIPT_DATE_MIN, RECEIPT_DATE_MAX);

        private final RandomString returnedFlagRandom;
        private final RandomString shipInstructionsRandom;
        private final RandomString shipModeRandom;

        private final RandomText commentRandom;

        private final double scaleFactor;
        private final long startIndex;

        private final long rowCount;

        private long index;
        private int orderDate;
        private int lineCount;
        private int lineNumber;

        public LineItemGeneratorIterator(Distributions distributions, TextPool textPool, double scaleFactor, long startIndex, long rowCount)
        {
            this.scaleFactor = scaleFactor;
            this.startIndex = startIndex;
            this.rowCount = rowCount;

            returnedFlagRandom = new RandomString( distributions.getReturnFlags());
            shipInstructionsRandom = new RandomString( distributions.getShipInstructions());
            shipModeRandom = new RandomString( distributions.getShipModes());
            commentRandom = new RandomText( textPool, COMMENT_AVERAGE_LENGTH);

            linePartKeyRandom = createPartKeyRandom(scaleFactor);

            // generate information for initial order
            orderDate = orderDateRandom.getValue(rowCount);
            lineCount = lineCountRandom.getValue(rowCount) - 1;
        }

        @Override
        public LineItem computeNext()
        {
            if (index >= rowCount) {
                return endOfData();
            }

            LineItem lineitem = makeLineitem(startIndex + index + 1);
            lineNumber++;

            // advance next row only when all lines for the order have been produced
            if (lineNumber > lineCount) {
                index++;
                // generate information for next order
                lineCount = lineCountRandom.getValue(index) - 1;
                orderDate = orderDateRandom.getValue(index);
                lineNumber = 0;
            }

            return lineitem;
        }

        public LineItem makeLineitem(long orderIndex)
        {
            long orderKey = makeOrderKey(index);

            int quantity = quantityRandom.getValue(index+lineNumber);
            int discount = discountRandom.getValue(index+lineNumber);
            int tax = taxRandom.getValue(index+lineNumber);

            long partKey = linePartKeyRandom.getValue(index+lineNumber);

            int supplierNumber = supplierNumberRandom.getValue(index+lineNumber);
            long supplierKey = selectPartSupplier(partKey, supplierNumber, scaleFactor);

            long partPrice = PartGenerator.calculatePartPrice(partKey);
            long extendedPrice = partPrice * quantity;

            int shipDate = shipDateRandom.getValue(index+lineNumber);
            shipDate += orderDate;
            int commitDate = commitDateRandom.getValue(index+lineNumber);
            commitDate += orderDate;
            int receiptDate = receiptDateRandom.getValue(index+lineNumber);
            receiptDate += shipDate;

            String returnedFlag;
            if (GenerateUtils.isInPast(receiptDate)) {
                returnedFlag = returnedFlagRandom.getValue(index+lineNumber);
            }
            else {
                returnedFlag = "N";
            }

            String status;
            if (GenerateUtils.isInPast(shipDate)) {
                status = "F";
            }
            else {
                status = "O";
            }

            String shipInstructions = shipInstructionsRandom.getValue(index+lineNumber);
            String shipMode = shipModeRandom.getValue(index+lineNumber);
            String comment = commentRandom.getValue(index+lineNumber);

            return new LineItem(orderIndex,
                    orderKey,
                    partKey,
                    supplierKey,
                    lineNumber + 1,
                    quantity,
                    extendedPrice,
                    discount,
                    tax,
                    returnedFlag,
                    status,
                    toEpochDate(shipDate),
                    toEpochDate(commitDate),
                    toEpochDate(receiptDate),
                    shipInstructions,
                    shipMode,
                    comment);
        }

        public boolean willSwitchToNewOrder(){
        	//System.out.println( lineNumber + " vs "+lineCount); 
            return (lineNumber == 0); 
        }

    }

    
    static RandomBoundedInt createQuantityRandom()
    {
        return new RandomBoundedInt(QUANTITY_MIN, QUANTITY_MAX);
    }

    static RandomBoundedInt createDiscountRandom()
    {
        return new RandomBoundedInt(DISCOUNT_MIN, DISCOUNT_MAX);
    }

    static RandomBoundedInt createTaxRandom()
    {
        return new RandomBoundedInt(TAX_MIN, TAX_MAX);
    }

    static RandomBoundedLong createPartKeyRandom(double scaleFactor)
    {
        return new RandomBoundedLong(scaleFactor >= 30000, PART_KEY_MIN, (long) (PartGenerator.SCALE_BASE * scaleFactor));
    }

    static RandomBoundedInt createShipDateRandom()
    {
        return new RandomBoundedInt( SHIP_DATE_MIN, SHIP_DATE_MAX);
    }
}
