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

import static com.google.common.base.Preconditions.checkNotNull;
import new_bench.types.AbstractEntity;
import new_bench.types.EntityInstance;
import new_bench.types.TpchDate;
import new_bench.types.TpchMoney;

public class LineItem extends AbstractEntity{
    private final long orderKey;
    private final long partKey;
    private final long supplierKey;
    private final long lineNumber;
    private final long quantity;
    private final TpchMoney extendedPrice;
    private final TpchMoney discount;
    private final TpchMoney tax;
    private final String returnFlag;
    private final String status;
    private final TpchDate shipDate;
    private final TpchDate commitDate;
    private final TpchDate receiptDate;
    private final String shipInstructions;
    private final String shipMode;
    private final String comment;

    public static String headers[] = {"orderKey","partKey","supplierKey","lineNumber","quantity","extendedPrice","discount","tax","returnFlag","status","shipDate","commitDate","receiptDate","shipInstructions","shipMode","comment"}; 
    public static String types[] = {"n","n","n","n","n","m","m", "m","s", "s", "d", "d", "d", "s", "s", "s"};
    
    public LineItem(long rowNumber,
            long orderKey,
            long partKey,
            long supplierKey,
            long lineNumber,
            long quantity,
            long extendedPrice,
            long discount,
            long tax,
            String returnFlag,
            String status,
            int shipDate,
            int commitDate,
            int receiptDate,
            String shipInstructions,
            String shipMode,
            String comment)
    {
    	super(rowNumber);
    	String[] values = new String[16];
    	this.relationName = "LineItem";
    	
    	values[0] = "" + (this.orderKey = orderKey);
    	values[1] = "" + (this.partKey = partKey);
    	values[2] = "" + (this.supplierKey = supplierKey);
    	values[3] = "" + (this.lineNumber = lineNumber);
    	values[4] = "" + (this.quantity = quantity);
    	values[5] = "" + (this.extendedPrice = new TpchMoney(extendedPrice));
    	values[6] = "" + (this.discount = new TpchMoney(discount));
    	values[7] = "" + (this.tax = new TpchMoney(tax));
    	values[8] = this.returnFlag = checkNotNull(returnFlag, "returnFlag is null");
    	values[9] = this.status = checkNotNull(status, "status is null");
        values[10] = "" + (this.shipDate = new TpchDate(shipDate));
        values[11] = "" + (this.commitDate = new TpchDate(commitDate));
        values[12] = "" + (this.receiptDate = new TpchDate(receiptDate));
        values[13] = this.shipInstructions = checkNotNull(shipInstructions, "shipInstructions is null");
        values[14] = this.shipMode = checkNotNull(shipMode, "shipMode is null");
        values[15] = this.comment = checkNotNull(comment, "comment is null");
        entity = new EntityInstance(relationName, headers, types, values); 
    }

    @Override
    public long getRowNumber()
    {
        return rowNumber;
    }

    public long getOrderKey()
    {
        return orderKey;
    }

    public long getPartKey()
    {
        return partKey;
    }

    public long getSupplierKey()
    {
        return supplierKey;
    }

    public long getLineNumber()
    {
        return lineNumber;
    }

    public long getQuantity()
    {
        return quantity;
    }

    public double getExtendedPrice()
    {
        return extendedPrice.getValue() / 100.0;
    }

    public long getExtendedPriceInCents()
    {
        return extendedPrice.getValue();
    }

    public double getDiscount()
    {
        return discount.getValue() / 100.0;
    }

    public long getDiscountPercent()
    {
        return discount.getValue();
    }

    public double getTax()
    {
        return tax.getValue() / 100.0;
    }

    public long getTaxPercent()
    {
        return tax.getValue();
    }

    public String getReturnFlag()
    {
        return returnFlag;
    }

    public String getStatus()
    {
        return status;
    }

    public int getShipDate()
    {
        return shipDate.getValue();
    }

    public int getCommitDate()
    {
        return commitDate.getValue();
    }

    public int getReceiptDate()
    {
        return receiptDate.getValue();
    }

    public String getShipInstructions()
    {
        return shipInstructions;
    }

    public String getShipMode()
    {
        return shipMode;
    }

    public String getComment()
    {
        return comment;
    }

}
