/*-----------------------------------------------------------

-- PURPOSE

--    BasicSimpleService的工场类，现在实现的Service较少，所以没有体现出它的价值

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.service;


public class ServiceFactory {
	public static ServiceFactory instant(){
		return new ServiceFactory();
	}
	
	private ServiceFactory(){}
	
	public IService<?> createService(int type){
		IService<?> service = null;
		switch(type){
		case IService.SIMPLE:
            service = createBasicSimpleService();
            break;
		}
		return service;
	}
	
	private BasicSimpleService createBasicSimpleService(){
		return new BasicSimpleService();
	}
	
}