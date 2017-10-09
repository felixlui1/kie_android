/*-----------------------------------------------------------

-- PURPOSE

--    KoJobItem工单的数据封装类。

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.model.item;

public class KoJobItem extends Item {
	
	public String wipEntityId;
	public String wipEntityName;
	
	public String saItem;
	public String saItemDesc;
	
	public String dffCpnNumber;
	
	public String dffCustomerspec;//其它要求
	public String dffMfgSpec;//工序要求
	
	public String custNumber;
	
	public int incompleteQuantity;
	public int startQuantity;
	public int quantityCompleted;
	public int quantityScrapped;
	
	public int primaryItemId;
	public String commonRoutingSequenceId;
	
	public String curOperationId;
	
	public String organizationId;
}
