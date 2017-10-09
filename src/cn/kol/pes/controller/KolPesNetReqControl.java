/*-----------------------------------------------------------

-- PURPOSE

--    KolPesNetReqControl是所有网络请求帮助类.由于 此应用的网络请求数量相对较少，所以我们采用一个帮助类管理所有网络请求。每个界面调用自己需要的请求即可。

-- History

--	  09-Sep-14  LiZheng  Created.

------------------------------------------------------------*/

package cn.kol.pes.controller;

import java.io.InputStream;
import java.util.List;

import android.util.Xml;
import android.util.Xml.Encoding;
import cn.kol.common.util.CacheUtils;
import cn.kol.common.util.LogUtils;
import cn.kol.pes.controllerback.IKolPesControlBack;
import cn.kol.pes.model.NetworkManager;
import cn.kol.pes.model.NetworkManager.OnDataParseListener;
import cn.kol.pes.model.NetworkManager.OnHttpDownloadListener;
import cn.kol.pes.model.item.KoAssetCheckCheckItem;
import cn.kol.pes.model.item.KoAssetCheckSetAssetCheckItem;
import cn.kol.pes.model.param.KoAssetCheckAddParams;
import cn.kol.pes.model.param.KoAssetCheckAssetListParams;
import cn.kol.pes.model.param.KoAssetCheckCancelCheckAssetParams;
import cn.kol.pes.model.param.KoAssetCheckCheckItemParams;
import cn.kol.pes.model.param.KoAssetCheckGetAssetInfoParams;
import cn.kol.pes.model.param.KoAssetCheckGetChangedPartsParams;
import cn.kol.pes.model.param.KoAssetCheckGetSetAssetListParams;
import cn.kol.pes.model.param.KoAssetCheckSubmitCheckParams;
import cn.kol.pes.model.param.KoAssetCheckUpdateSetAssetListParams;
import cn.kol.pes.model.param.KoAssetGetAssetInfoParams;
import cn.kol.pes.model.param.KoAssetGetCheckErrorListParams;
import cn.kol.pes.model.param.KoAssetInsertPicPathParams;
import cn.kol.pes.model.param.KoAssetMachineFailListParams;
import cn.kol.pes.model.param.KoAssetRepairParams;
import cn.kol.pes.model.param.KoDataEnableParams;
import cn.kol.pes.model.param.KoGeJobParams;
import cn.kol.pes.model.param.KoGetOpMoveAllListParams;
import cn.kol.pes.model.param.KoGetOpParams;
import cn.kol.pes.model.param.KoGetStartedOpListParams;
import cn.kol.pes.model.param.KoLoginParams;
import cn.kol.pes.model.param.KoOpCheckBeforeStartParams;
import cn.kol.pes.model.param.KoOpDeleteOpParams;
import cn.kol.pes.model.param.KoOpEndParams;
import cn.kol.pes.model.param.KoOpMaxQuanParams;
import cn.kol.pes.model.param.KoOpStartParams;
import cn.kol.pes.model.param.KoPushMsgParams;
import cn.kol.pes.model.param.KoQaInsertDataParams;
import cn.kol.pes.model.param.KoQaListByPlanIdParams;
import cn.kol.pes.model.param.KoQaListManualAddParams;
import cn.kol.pes.model.param.KoQaListNeedFillParams;
import cn.kol.pes.model.param.KoUpdateApkParams;
import cn.kol.pes.model.parser.KoAssetCheckAddParser;
import cn.kol.pes.model.parser.KoAssetCheckAssetItemParser;
import cn.kol.pes.model.parser.KoAssetCheckChangedPartsListParser;
import cn.kol.pes.model.parser.KoAssetCheckCheckItemParser;
import cn.kol.pes.model.parser.KoAssetCheckRepairParser;
import cn.kol.pes.model.parser.KoAssetCheckSetAssetCheckItemParser;
import cn.kol.pes.model.parser.KoAssetCheckSubmitCheckParser;
import cn.kol.pes.model.parser.KoAssetCheckUpdateSetAssetCheckParser;
import cn.kol.pes.model.parser.KoAssetGetAssetInfoParser;
import cn.kol.pes.model.parser.KoAssetGetCheckErrorListParser;
import cn.kol.pes.model.parser.KoAssetInsertPicPathParser;
import cn.kol.pes.model.parser.KoAssetMachineFailListParser;
import cn.kol.pes.model.parser.KoCheckAssetCancelCheckAssetParser;
import cn.kol.pes.model.parser.KoDataEnableParser;
import cn.kol.pes.model.parser.KoGetJobListParser;
import cn.kol.pes.model.parser.KoGetOpListParser;
import cn.kol.pes.model.parser.KoGetOpStartedListParser;
import cn.kol.pes.model.parser.KoLoginParser;
import cn.kol.pes.model.parser.KoOpCheckBeforeStartParser;
import cn.kol.pes.model.parser.KoOpDeleteOpParser;
import cn.kol.pes.model.parser.KoOpEndParser;
import cn.kol.pes.model.parser.KoOpMaxQuanParser;
import cn.kol.pes.model.parser.KoOpStartParser;
import cn.kol.pes.model.parser.KoPushMsgListParser;
import cn.kol.pes.model.parser.KoQaInsertDataParser;
import cn.kol.pes.model.parser.KoQaListNeedFillParser;
import cn.kol.pes.model.parser.KoUpdateApkParser;
import cn.kol.pes.model.parser.adapter.KoAssetCheckAddAdapter;
import cn.kol.pes.model.parser.adapter.KoAssetCheckAssetItemAdapter;
import cn.kol.pes.model.parser.adapter.KoAssetCheckCancelCheckAssetAdapter;
import cn.kol.pes.model.parser.adapter.KoAssetCheckChangedPartsListAdapter;
import cn.kol.pes.model.parser.adapter.KoAssetCheckCheckItemAdapter;
import cn.kol.pes.model.parser.adapter.KoAssetCheckRepairAdapter;
import cn.kol.pes.model.parser.adapter.KoAssetCheckSetAssetCheckAdapter;
import cn.kol.pes.model.parser.adapter.KoAssetCheckSubmitCheckAdapter;
import cn.kol.pes.model.parser.adapter.KoAssetCheckUpdateSetAssetCheckAdapter;
import cn.kol.pes.model.parser.adapter.KoAssetGetAssetInfoAdapter;
import cn.kol.pes.model.parser.adapter.KoAssetGetCheckErrorListAdapter;
import cn.kol.pes.model.parser.adapter.KoAssetInsertPicPathAdapter;
import cn.kol.pes.model.parser.adapter.KoAssetMachineFailListAdapter;
import cn.kol.pes.model.parser.adapter.KoDataEnableAdapter;
import cn.kol.pes.model.parser.adapter.KoGetJobListAdapter;
import cn.kol.pes.model.parser.adapter.KoGetOpListAdapter;
import cn.kol.pes.model.parser.adapter.KoGetOpStartedListAdapter;
import cn.kol.pes.model.parser.adapter.KoLoginAdapter;
import cn.kol.pes.model.parser.adapter.KoOpCheckBeforeStartAdapter;
import cn.kol.pes.model.parser.adapter.KoOpDeleteOpAdapter;
import cn.kol.pes.model.parser.adapter.KoOpEndAdapter;
import cn.kol.pes.model.parser.adapter.KoOpMaxQuanAdapter;
import cn.kol.pes.model.parser.adapter.KoOpStartAdapter;
import cn.kol.pes.model.parser.adapter.KoPushMsgAdapter;
import cn.kol.pes.model.parser.adapter.KoQaInsertDataAdapter;
import cn.kol.pes.model.parser.adapter.KoQaListNeedFillAdapter;
import cn.kol.pes.model.parser.adapter.KoUpdateApkAdapter;
import cn.kol.pes.service.BasicSimpleService;
import cn.kol.pes.service.IService;
import cn.kol.pes.service.ServiceFactory;


public class KolPesNetReqControl extends BaseControl implements OnHttpDownloadListener, OnDataParseListener {

    private static final String tag = "KolPesNetReqControl";
    
    private static final String SUC_CODE = "0";
    
    private IKolPesControlBack mIKolPesNetReqControlBack;//是具体网络请求的回调
    
    private BasicSimpleService mBaseService;
    
    private int mDataEnableServiceId = -1;
    private int mLoginServiceId = -1;
    private int mGetJobListServiceId = -1;
    private int mGetOpListServiceId = -1;
    private int mAssetCheckAddServiceId = -1;
    private int mAssetInsertPicPathServiceId = -1;
    private int mGetAssetCheckListServiceId = -1;
    private int mAssetGetMachineFailListServiceId = -1;
    private int mAssetRepairServiceId = -1;
    private int mAssetGetAssetInfoServiceId = -1;
    private int mOpMaxQuanServiceId = -1;
    private int mOpStartServiceId = -1;
    private int mOpEndServiceId = -1;
    private int mGetOpStartedListServiceId = -1;
    private int mGetOpMoveAllListServiceId = -1;
    private int mQaListNeedFillListServiceId = -1;
    private int mQaListByPlanIdListServiceId = -1;
    private int mQaListManualAddListServiceId = -1;
    private int mQaInsertDataServiceId = -1;
    private int mGetMsgListServiceId = -1;
    private int mUpdateApkServiceId = -1;
    
    private int mOpCheckServiceId = -1;
    private int mOpDeleteServiceId = -1;
    
    private int mAssetCheckGetAssetInfoServiceId = -1;
    private int mAssetCheckCancelCheckAssetServiceId = -1;
    private int mGetCheckItemListServiceId = -1;
    
    private int mAssetCheckSubmitCheckServiceId = -1;
    
    private int mAssetCheckGetAssetListServiceId = -1;
    
    private int mAssetCheckGetSetAssetListServiceId = -1;
    
    private int mAssetCheckUpdateSetAssetListServiceId = -1;
    
    private int mAssetCheckGetChangedPartsListServiceId = -1;
    
    private KoDataEnableAdapter mKoDataEnableAdapter = new KoDataEnableAdapter();
    private KoLoginAdapter mLoginAdapter = new KoLoginAdapter();
    private KoGetJobListAdapter mUpdateJobAdapter = new KoGetJobListAdapter();
    private KoGetOpListAdapter mKoGetOpListAdapter = new KoGetOpListAdapter();
    private KoAssetCheckAddAdapter mKoAssetCheckAddAdapter = new KoAssetCheckAddAdapter();
    private KoAssetInsertPicPathAdapter mKoAssetInsertPicPathAdapter = new KoAssetInsertPicPathAdapter();
    private KoAssetGetCheckErrorListAdapter mKoAssetGetCheckErrorListAdapter = new KoAssetGetCheckErrorListAdapter();
    private KoAssetMachineFailListAdapter mKoAssetMachineFailListAdapter = new KoAssetMachineFailListAdapter();
    private KoAssetCheckRepairAdapter mKoAssetCheckRepairAdapter = new KoAssetCheckRepairAdapter();
    private KoAssetGetAssetInfoAdapter mKoAssetGetAssetInfoAdapter = new KoAssetGetAssetInfoAdapter();
    private KoOpMaxQuanAdapter mKoOpMaxQuanAdapter = new KoOpMaxQuanAdapter();
    private KoOpStartAdapter mKoOpStartAdapter = new KoOpStartAdapter();
    private KoOpEndAdapter mKoOpEndAdapter = new KoOpEndAdapter();
    private KoGetOpStartedListAdapter mKoGetOpStartedListAdapter = new KoGetOpStartedListAdapter();
    private KoGetOpStartedListAdapter mKoGetOpMoveAllListAdapter = new KoGetOpStartedListAdapter();
    private KoQaListNeedFillAdapter mKoQaListNeedFillAdapter = new KoQaListNeedFillAdapter();
    private KoQaListNeedFillAdapter mKoQaListManualAdapter = new KoQaListNeedFillAdapter();
    private KoQaListNeedFillAdapter mKoQaListByPlanIdAdapter = new KoQaListNeedFillAdapter();
    private KoQaInsertDataAdapter mKoQaInsertDataAdapter = new KoQaInsertDataAdapter();
    private KoPushMsgAdapter mKoPushMsgListAdapter = new KoPushMsgAdapter();
    private KoUpdateApkAdapter mKoUpdateApkAdapter = new KoUpdateApkAdapter();
    
    private KoOpCheckBeforeStartAdapter mKoOpCheckBeforeStartAdapter = new KoOpCheckBeforeStartAdapter();
    private KoOpDeleteOpAdapter mKoOpDeleteOpAdapter = new KoOpDeleteOpAdapter();
    
    private KoAssetCheckCheckItemAdapter mKoAssetCheckCheckItemAdapter = new KoAssetCheckCheckItemAdapter();
    private KoAssetGetAssetInfoAdapter mKoAssetCheckGetAssetInfoAdapter = new KoAssetGetAssetInfoAdapter();
    private KoAssetCheckSubmitCheckAdapter mKoAssetCheckSubmitCheckAdapter = new KoAssetCheckSubmitCheckAdapter();
    
    private KoAssetCheckAssetItemAdapter mKoAssetCheckAssetItemAdapter = new KoAssetCheckAssetItemAdapter();
    private KoAssetCheckSetAssetCheckAdapter mKoAssetCheckSetAssetCheckAdapter = new KoAssetCheckSetAssetCheckAdapter();
    private KoAssetCheckUpdateSetAssetCheckAdapter mKoAssetCheckUpdateSetAssetCheckAdapter = new KoAssetCheckUpdateSetAssetCheckAdapter();
    private KoAssetCheckChangedPartsListAdapter mKoAssetCheckChangedPartsListAdapter = new KoAssetCheckChangedPartsListAdapter();
    
    private KoAssetCheckCancelCheckAssetAdapter mKoAssetCheckCancelCheckAssetAdapter = new KoAssetCheckCancelCheckAssetAdapter();
    
    //任何Activity调用这个帮助类的同时，必须传入回调类的实例
    public KolPesNetReqControl(IKolPesControlBack iKolPesNetReqControlBack) {
    	NetworkManager.instance().stopRunningTask();
    	mBaseService = (BasicSimpleService) ServiceFactory.instant().createService(IService.SIMPLE);
        this.mIKolPesNetReqControlBack = iKolPesNetReqControlBack;
    }
    
    //设置回调的接口实现
    public void setBack(IKolPesControlBack iKolPesNetReqControlBack) {
    	this.mIKolPesNetReqControlBack = iKolPesNetReqControlBack;
    }
    
    //判断服务器数据库是否在更新
    public void isDataEnable() {
    	mDataEnableServiceId = mBaseService.getDataFromService(new KoDataEnableParams(), this, this);
    }
    
    //登陆请求
    public void login(String userId) {
    	mLoginServiceId = mBaseService.getDataFromService(new KoLoginParams(userId), this, this);
    }
    
    //获取工单
    public void getJobList(String wipId) {
    	mGetJobListServiceId = mBaseService.getDataFromService(new KoGeJobParams(wipId), this, this);

    	LogUtils.e(tag, "getJobList():mGetJobListServiceId="+mGetJobListServiceId+", wipId="+wipId);
    }
    
    //获取工序
    public void getOp(String seqId) {
    	mGetOpListServiceId = mBaseService.getDataFromService(new KoGetOpParams(seqId), this, this);
    }
    
    //添加点检设备记录
    public void assetCheckAdd(String CREATION_DATE, String CREATED_BY, 
							 String LAST_UPDATE_DATE, String LAST_UPDATED_BY,
							 String ASSET_ID, String CHECK_TIME, String CHECK_RESULT, 
							 String EST_REPAIR_START, String EST_REPAIR_END, 
							 String CHECK_REMARKS) {
    	
    	mAssetCheckAddServiceId = mBaseService.getDataFromService(new KoAssetCheckAddParams(CREATION_DATE, CREATED_BY, LAST_UPDATE_DATE, LAST_UPDATED_BY, ASSET_ID, CHECK_TIME, CHECK_RESULT, EST_REPAIR_START, EST_REPAIR_END, CHECK_REMARKS), this, this);
    }
    
    //上传图片信息
    public void assetInserPicPath(String checkId, String picPathDescList, String isEnd, String isSeq) {
    	mAssetInsertPicPathServiceId = mBaseService.getDataFromService(new KoAssetInsertPicPathParams(checkId, picPathDescList, isEnd, isSeq), this, this);
    }
    
    //获取存在故障的设备的点检列表
    public void getAssetCheckErrorList() {
    	mGetAssetCheckListServiceId = mBaseService.getDataFromService(new KoAssetGetCheckErrorListParams(), this, this);
    }
    
    //获取相应设备的故障类型
    public void getAssetMachineFailList(String tagNum) {
    	mAssetGetMachineFailListServiceId = mBaseService.getDataFromService(new KoAssetMachineFailListParams(tagNum), this, this);
    }
    
    //更新设备维修信息
    public void assetRepair(String CHECK_ID, String LAST_UPDATE_DATE, String LAST_UPDATED_BY,
    					    String ACT_REPAIR_START, String ACT_REPAIR_END, String DOWN_TIME, 
							String REPAIR_REMARKS, String FAILURE_CODE, String changedParts, String opCode) {
    	
    	mAssetRepairServiceId = mBaseService.getDataFromService(
											    			new KoAssetRepairParams(CHECK_ID, LAST_UPDATE_DATE, LAST_UPDATED_BY, 
																	    			ACT_REPAIR_START, ACT_REPAIR_END, DOWN_TIME, 
																	    			REPAIR_REMARKS, FAILURE_CODE, changedParts, opCode), 
														    this, this);
    }
    
    //根据设备id获取设备相关信息
    public void assetGetAssetInfo(String id) {
    	mAssetGetAssetInfoServiceId = mBaseService.getDataFromService(new KoAssetGetAssetInfoParams(id), this, this);
    }
    
    //开始工序前，获取最大可投入数
    public void opGetMaxQuan(String wipEntityName, String wipEntityId, String seqId, String fmOperationCode, String curOperationId, String canJump) {
    	mOpMaxQuanServiceId = mBaseService.getDataFromService(new KoOpMaxQuanParams(wipEntityName, wipEntityId, seqId, fmOperationCode, curOperationId, canJump), this, this);
    }
    
    //开始工序
    public void opStart(String createdBy, String lastUpdatedBy, 
						String wipEntityId, String wipEntityName, String fmOperationCode, String curOperationId,
						String trxQuantity,
						List<String> assetIdList, String opStart, String seqId, String canJump) {
    	
    	mOpStartServiceId = mBaseService.getDataFromService(new KoOpStartParams(createdBy, lastUpdatedBy, 
																    			wipEntityId, wipEntityName, fmOperationCode, curOperationId,
																    			trxQuantity,
																    			assetIdList, opStart, seqId, canJump), 
														    this, this);
    }
    
    //完成工序
    public void opEnd(String transactionId, String scrapQuantity, String opEnd, String lastUpdatedBy, String organizationId, String wipId, String opCode) {
    	
    	mOpEndServiceId = mBaseService.getDataFromService(new KoOpEndParams(transactionId, scrapQuantity, opEnd, lastUpdatedBy, organizationId, wipId, opCode), 
														    this, this);
    }
    
    //获取已开启工序的列表
    public void getStartedOpList(String lastUpdatedByOrAssetId) {
    	
    	mGetOpStartedListServiceId = mBaseService.getDataFromService(new KoGetStartedOpListParams(lastUpdatedByOrAssetId), this, this);
    }
    
    //获取特定工序所有的工序列表
    public void getOpMoveAllList(String wipId) {
    	
    	mGetOpMoveAllListServiceId = mBaseService.getDataFromService(new KoGetOpMoveAllListParams(wipId), this, this);
    }
    
    //获取质量管理计划要填写的数据项信息  同时 检查是否可以完成工序
    public void getQaListNeedFill(String wipId, String opCode, String organizationId) {
    	mQaListNeedFillListServiceId = mBaseService.getDataFromService(new KoQaListNeedFillParams(wipId, opCode, organizationId), this, this);
    }
    
    //获取质量管理计划要填写的数据项信息
    public void getQaListByPlanId(String planId, String wipId, String opCode) {
    	mQaListByPlanIdListServiceId = mBaseService.getDataFromService(new KoQaListByPlanIdParams(planId, wipId, opCode), this, this);
    }
    
    //获取人工添加的质量收集计划
    public void getQaListManualAdd(String wipId, String opCode) {
    	mQaListManualAddListServiceId = mBaseService.getDataFromService(new KoQaListManualAddParams(wipId, opCode), this, this);
    }
    
    //提交质量管理计划数据
    public void submitQaData(String createStaffNo, String wipId, String opCode, String transactionId, String qaNvList, String qaChildNvList, boolean isManualAddedQa, String childPlanId) {
    	mQaInsertDataServiceId = mBaseService.getDataFromService(new KoQaInsertDataParams(createStaffNo, wipId, opCode, transactionId, qaNvList, qaChildNvList, isManualAddedQa, childPlanId), this, this);
    }
    
    //获取坏品推送消息
    public void getPushMsgList(String staffNo, String transId, boolean isNotice) {
    	mGetMsgListServiceId = mBaseService.getDataFromService(new KoPushMsgParams(staffNo, transId, isNotice), this, this);
    }
    
    //获取apk升级信息
    public void getUpdateApk(String apkPadVersion) {
    	mUpdateApkServiceId = mBaseService.getDataFromService(new KoUpdateApkParams(apkPadVersion), this, this);
    }
    
    //开始工序前检查条件
    public void opCheckBeforeStart(String wipEntityId, String wipEntityName, String fmOperationCode, String curOperationId, String seqId) {
    	mOpCheckServiceId = mBaseService.getDataFromService(new KoOpCheckBeforeStartParams(wipEntityId, wipEntityName, fmOperationCode, curOperationId, seqId), this, this);
    }
    
    //删除一个工序
    public void opDeleteAnOp(String transactionId) {
    	mOpDeleteServiceId = mBaseService.getDataFromService(new KoOpDeleteOpParams(transactionId), this, this);
    }
    
    //根据设备id获取设备相关信息
    public void assetCheckGetAssetInfo(String id) {
    	mAssetCheckGetAssetInfoServiceId = mBaseService.getDataFromService(new KoAssetCheckGetAssetInfoParams(id), this, this);
    }
    
    //取消某台设备的点检设置
    public void cancelCheckAsset(String id) {
    	mAssetCheckCancelCheckAssetServiceId = mBaseService.getDataFromService(new KoAssetCheckCancelCheckAssetParams(id), this, this);
    }
    
    //根据设备id获取设备的点检信息列表
    public void getCheckItemList(String assetId) {
    	mGetCheckItemListServiceId = mBaseService.getDataFromService(new KoAssetCheckCheckItemParams(assetId), this, this);
    }
    
    //提交点检的结果
    public void assetCheckSubmitCheck(String assetId, String userId, List<KoAssetCheckCheckItem> checkValueList) {
    	mAssetCheckSubmitCheckServiceId = mBaseService.getDataFromService(new KoAssetCheckSubmitCheckParams(userId, assetId, userId, checkValueList), this, this);
    }
    
    //获取“选择需要点检的机器”界面的数据
    public void assetCheckGetAssetItemList(String startDate, String endDate) {
    	mAssetCheckGetAssetListServiceId = mBaseService.getDataFromService(new KoAssetCheckAssetListParams(startDate, endDate), this, this);
    }
    
    //获取设置点检机器界面的数据
    public void assetCheckGetSetAssetCheckList(String date, String shift, String staffNo) {
    	mAssetCheckGetSetAssetListServiceId = mBaseService.getDataFromService(new KoAssetCheckGetSetAssetListParams(date, shift, staffNo), this, this);	
    }
    
    //更新待点检设备列表
    public void assetCheckUpdateSetAssetCheck(String checkDate, String shift, List<KoAssetCheckSetAssetCheckItem> assetList) {
    	mAssetCheckUpdateSetAssetListServiceId = mBaseService.getDataFromService(new KoAssetCheckUpdateSetAssetListParams(checkDate, shift, CacheUtils.getLoginUserInfo().staffNo, assetList), this, this);	
    }
    
    //获取零配件列表
    public void assetCheckGetChangedPartsList(String opCode) {
    	mAssetCheckGetChangedPartsListServiceId = mBaseService.getDataFromService(new KoAssetCheckGetChangedPartsParams(opCode), this, this);	
    }

    //这是网络请求的返回数据被解析完成后的回调，每个taskId对应一个请求线程.此处就可以将解析后的数据对应到相应的业务回调中了
    @Override
    public void onHttpDownLoadDone(int taskId, String result) {
    	
    	LogUtils.e(tag, "onHttpDownLoadDone():result="+result);
    	
    	if(!OnHttpDownloadListener.SUCCESS.equals(result)) {
    		return;
    	}
    	
        if(taskId == mLoginServiceId) {

        	mIKolPesNetReqControlBack.loginBack(mLoginAdapter.isSuccess() && OnHttpDownloadListener.SUCCESS.equals(result), mLoginAdapter, mLoginAdapter.getMessage());
        }
        else if(taskId == mGetJobListServiceId) {
        	
        	mIKolPesNetReqControlBack.getJobBack(SUC_CODE.equals(mUpdateJobAdapter.getCode()) && OnHttpDownloadListener.SUCCESS.equals(result), 
													 mUpdateJobAdapter.getList(), 
													 mUpdateJobAdapter.getMessage());
        }
        else if(taskId == mGetOpListServiceId) {
        	
        	mIKolPesNetReqControlBack.getOpBack(SUC_CODE.equals(mKoGetOpListAdapter.getCode()) && OnHttpDownloadListener.SUCCESS.equals(result), 
								        			mKoGetOpListAdapter.getList(), 
								        			mKoGetOpListAdapter.getMessage());
        }
        else if(taskId == mAssetCheckAddServiceId) {
        	
        	mIKolPesNetReqControlBack.assetCheckAddBack(SUC_CODE.equals(mKoAssetCheckAddAdapter.getCode()), mKoAssetCheckAddAdapter.checkId, mKoAssetCheckAddAdapter.getMessage());
        }
        else if(taskId == mAssetInsertPicPathServiceId) {
        	
        	mIKolPesNetReqControlBack.assetInsertPicPathBack(SUC_CODE.equals(mKoAssetInsertPicPathAdapter.getCode()), mKoAssetInsertPicPathAdapter.getMessage());
        }
        else if(taskId == mGetAssetCheckListServiceId) {
        	mIKolPesNetReqControlBack.assetCheckGetErrorListBack(
        												SUC_CODE.equals(mKoAssetGetCheckErrorListAdapter.getCode()), 
									        			mKoAssetGetCheckErrorListAdapter.getMessage(), 
									        			mKoAssetGetCheckErrorListAdapter.getList());
        }
        else if(taskId == mAssetGetMachineFailListServiceId) {
        	mIKolPesNetReqControlBack.assetGetMachineFailListBack(
											        			SUC_CODE.equals(mKoAssetMachineFailListAdapter.getCode()), 
											        			mKoAssetMachineFailListAdapter.getMessage(), 
											        			mKoAssetMachineFailListAdapter.getList());
        }
        else if(taskId == mAssetRepairServiceId) {
        	mIKolPesNetReqControlBack.assetCheckRepairBack(SUC_CODE.equals(mKoAssetCheckRepairAdapter.getCode()),
        												   mKoAssetCheckRepairAdapter.getMessage());
        }
        else if(taskId == mAssetGetAssetInfoServiceId) {
        	mIKolPesNetReqControlBack.assetGetAssetInfoBack(SUC_CODE.equals(mKoAssetGetAssetInfoAdapter.getCode()), 
										        			mKoAssetGetAssetInfoAdapter.getMessage(), 
										        			mKoAssetGetAssetInfoAdapter);
        }
        else if(taskId == mDataEnableServiceId) {
        	mIKolPesNetReqControlBack.dateEnableBack(SUC_CODE.equals(mKoDataEnableAdapter.getCode()), 
        											 mKoDataEnableAdapter.getWeekMap(),
        											 mKoDataEnableAdapter.getMessage());
        }
        else if(taskId == mOpMaxQuanServiceId) {
        	mIKolPesNetReqControlBack.opGetMaxQuanBack(SUC_CODE.equals(mKoOpMaxQuanAdapter.getCode()), 
        											   mKoOpMaxQuanAdapter.maxQuan,
        											   mKoOpMaxQuanAdapter.getOpAssetList(),
        											   mKoOpMaxQuanAdapter.getMessage());
        }
        else if(taskId == mOpStartServiceId) {
        	mIKolPesNetReqControlBack.opStartBack(SUC_CODE.equals(mKoOpStartAdapter.getCode()), 
									        			mKoOpStartAdapter.transactionId,
									        			mKoOpStartAdapter.getMessage());
        }
        else if(taskId == mOpEndServiceId) {
        	mIKolPesNetReqControlBack.opEndBack(SUC_CODE.equals(mKoOpEndAdapter.getCode()), 
								        			mKoOpEndAdapter.transactionId,
								        			mKoOpEndAdapter.getMessage());
        }
        else if(taskId == mGetOpStartedListServiceId) {
        	mIKolPesNetReqControlBack.opStartedListBack(SUC_CODE.equals(mKoGetOpStartedListAdapter.getCode()), 
									        			mKoGetOpStartedListAdapter.getList(),
									        			mKoGetOpStartedListAdapter.isOpCompleted,
									        			mKoGetOpStartedListAdapter.curWorkingOpCode,
									        			mKoGetOpStartedListAdapter.getMessage());
        }
        else if(taskId == mGetOpMoveAllListServiceId) {
        	mIKolPesNetReqControlBack.opMoveAllListBack(SUC_CODE.equals(mKoGetOpMoveAllListAdapter.getCode()), 
									        			mKoGetOpMoveAllListAdapter.getList(),
									        			mKoGetOpMoveAllListAdapter.getMessage());
        }
        else if(taskId == mQaListNeedFillListServiceId) {
        	mIKolPesNetReqControlBack.qaListNeedFillBack(SUC_CODE.equals(mKoQaListNeedFillAdapter.getCode()), 
        												mKoQaListNeedFillAdapter.mIsLastSeq,
        												mKoQaListNeedFillAdapter.incompleteQuan,
        												mKoQaListNeedFillAdapter.minStartTime,
        												mKoQaListNeedFillAdapter.maxEndTime,
														mKoQaListNeedFillAdapter.getList(),
														mKoQaListNeedFillAdapter.getChildPlanIdList(),
														mKoQaListNeedFillAdapter.getMessage(),
														mKoQaListNeedFillAdapter.timeBufferOpEnd,
														mKoQaListNeedFillAdapter.scrapQuanTotal);
        }
        else if(taskId == mQaListByPlanIdListServiceId) {
        	mIKolPesNetReqControlBack.qaListByPlanIdBack(SUC_CODE.equals(mKoQaListByPlanIdAdapter.getCode()), 
									        			 mKoQaListByPlanIdAdapter.incompleteQuan,
									        			 mKoQaListByPlanIdAdapter.minStartTime,
														 mKoQaListByPlanIdAdapter.maxEndTime,
        												 mKoQaListByPlanIdAdapter.getList(),
        												 mKoQaListByPlanIdAdapter.getMessage());
        }
        else if(taskId == mQaListManualAddListServiceId) {
        	mIKolPesNetReqControlBack.qaListManualAddBack(SUC_CODE.equals(mKoQaListManualAdapter.getCode()), 
										        			mKoQaListManualAdapter.incompleteQuan,
										        			mKoQaListManualAdapter.minStartTime,
															mKoQaListManualAdapter.maxEndTime,
										        			mKoQaListManualAdapter.getList(),
										        			mKoQaListManualAdapter.getMessage());
        }
        else if(taskId == mQaInsertDataServiceId) {
        	mIKolPesNetReqControlBack.submitQaDataBack(SUC_CODE.equals(mKoQaInsertDataAdapter.getCode()), 
        											   mKoQaInsertDataAdapter.getMessage());
        }
        else if(taskId == mGetMsgListServiceId) {
			mIKolPesNetReqControlBack.getPushMsgListBack(SUC_CODE.equals(mKoPushMsgListAdapter.getCode()), 
														mKoPushMsgListAdapter.getList(),
														mKoPushMsgListAdapter.getMessage());
		}
        else if(taskId == mUpdateApkServiceId) {
        	mIKolPesNetReqControlBack.getUpdateApkBack(SUC_CODE.equals(mKoUpdateApkAdapter.getCode()), 
									        			mKoUpdateApkAdapter,
									        			mKoUpdateApkAdapter.getMessage());
        }
        else if(taskId == mOpCheckServiceId) {
        	mIKolPesNetReqControlBack.getOpCheckBeforeStartBack(SUC_CODE.equals(mKoOpCheckBeforeStartAdapter.getCode()), 
        														mKoOpCheckBeforeStartAdapter.timeBufferOpStart,
        														mKoOpCheckBeforeStartAdapter.getMessage());
        }
        else if(taskId == mOpDeleteServiceId) {
        	mIKolPesNetReqControlBack.getOpDeleteBack(SUC_CODE.equals(mKoOpDeleteOpAdapter.getCode()), 
        											  mKoOpDeleteOpAdapter.getMessage());
        }
        else if(taskId == mGetCheckItemListServiceId) {
        	mIKolPesNetReqControlBack.getCheckItemListDataBack(SUC_CODE.equals(mKoAssetCheckCheckItemAdapter.getCode()), 
        													   mKoAssetCheckCheckItemAdapter.getList(),
        	SUC_CODE.equals(mKoAssetCheckCheckItemAdapter.getCode())?mKoAssetCheckCheckItemAdapter.assetLastCheckTime:mKoAssetCheckCheckItemAdapter.getMessage());
        }
        else if(taskId == mAssetCheckGetAssetInfoServiceId) {
        	mIKolPesNetReqControlBack.assetCheckGetAssetInfoBack(SUC_CODE.equals(mKoAssetCheckGetAssetInfoAdapter.getCode()), 
											    			mKoAssetCheckGetAssetInfoAdapter.getMessage(), 
											    			mKoAssetCheckGetAssetInfoAdapter);
        }
        else if(taskId == mAssetCheckSubmitCheckServiceId) {
        	mIKolPesNetReqControlBack.assetCheckSubmitCheckBack(SUC_CODE.equals(mKoAssetCheckSubmitCheckAdapter.getCode()), 
        														mKoAssetCheckSubmitCheckAdapter.getMessage());
        }
        else if(taskId == mAssetCheckGetAssetListServiceId) {
        	mIKolPesNetReqControlBack.assetCheckAssetListBack(SUC_CODE.equals(mKoAssetCheckAssetItemAdapter.getCode()), 
											        			mKoAssetCheckAssetItemAdapter.getList(), 
											        			mKoAssetCheckAssetItemAdapter.getMessage());
        }
        else if(taskId == mAssetCheckGetSetAssetListServiceId) {
        	mIKolPesNetReqControlBack.assetCheckGetSetAssetCheckListBack(SUC_CODE.equals(mKoAssetCheckSetAssetCheckAdapter.getCode()), 
        																mKoAssetCheckSetAssetCheckAdapter.getList(), 
        																mKoAssetCheckSetAssetCheckAdapter.getMessage());
        }
        else if(taskId == mAssetCheckUpdateSetAssetListServiceId) {
        	mIKolPesNetReqControlBack.assetCheckUpdateSetAssetCheckListBack(SUC_CODE.equals(mKoAssetCheckUpdateSetAssetCheckAdapter.getCode()), 
        																	mKoAssetCheckUpdateSetAssetCheckAdapter.getMessage());
        }
        else if(taskId == mAssetCheckGetChangedPartsListServiceId) {
        	mIKolPesNetReqControlBack.assetCheckGetChangedPartsBack(SUC_CODE.equals(mKoAssetCheckChangedPartsListAdapter.getCode()), 
												        			mKoAssetCheckChangedPartsListAdapter.getList(), 
												        			mKoAssetCheckChangedPartsListAdapter.getMessage());
        }
        else if(taskId == mAssetCheckCancelCheckAssetServiceId) {
        	mIKolPesNetReqControlBack.assetCheckCancelCheckAssetBack(SUC_CODE.equals(mKoAssetCheckCancelCheckAssetAdapter.getCode()), 
        															 mKoAssetCheckCancelCheckAssetAdapter.getMessage());
        }
    }
    
    //网络请求的回调，此处的数据还未被解析。需在此处根据不同的id进行区分解析。
	@Override
	public String onDataParse(int id, InputStream is) {
		
		LogUtils.e(tag, "onDataParse()");
		
		if(mGetJobListServiceId == id) {
			try {
				mUpdateJobAdapter.clear();
				Xml.parse(is, Encoding.UTF_8, new KoGetJobListParser(mUpdateJobAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(mLoginServiceId == id) {
			try {
				mLoginAdapter = new KoLoginAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoLoginParser(mLoginAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(mGetOpListServiceId == id) {
			try {
				mKoGetOpListAdapter.clear();
				Xml.parse(is, Encoding.UTF_8, new KoGetOpListParser(mKoGetOpListAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(mAssetCheckAddServiceId == id) {
			try {
				mKoAssetCheckAddAdapter = new KoAssetCheckAddAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoAssetCheckAddParser(mKoAssetCheckAddAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
        }
		else if(mAssetInsertPicPathServiceId == id) {
			try {
				mKoAssetInsertPicPathAdapter = new KoAssetInsertPicPathAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoAssetInsertPicPathParser(mKoAssetInsertPicPathAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
        }
		else if(id == mGetAssetCheckListServiceId) {
			try {
				mKoAssetGetCheckErrorListAdapter.clear();
				Xml.parse(is, Encoding.UTF_8, new KoAssetGetCheckErrorListParser(mKoAssetGetCheckErrorListAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
        }
		else if(id == mAssetGetMachineFailListServiceId) {
			try {
				mKoAssetMachineFailListAdapter = new KoAssetMachineFailListAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoAssetMachineFailListParser(mKoAssetMachineFailListAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mAssetRepairServiceId) {
			try {
				mKoAssetCheckRepairAdapter = new KoAssetCheckRepairAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoAssetCheckRepairParser(mKoAssetCheckRepairAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
        }
		else if(id == mAssetGetAssetInfoServiceId) {
			try {
				mKoAssetGetAssetInfoAdapter = new KoAssetGetAssetInfoAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoAssetGetAssetInfoParser(mKoAssetGetAssetInfoAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mDataEnableServiceId) {
			try {
				mKoDataEnableAdapter = new KoDataEnableAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoDataEnableParser(mKoDataEnableAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mOpMaxQuanServiceId) {
			try {
				mKoOpMaxQuanAdapter = new KoOpMaxQuanAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoOpMaxQuanParser(mKoOpMaxQuanAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mOpStartServiceId) {
			try {
				mKoOpStartAdapter = new KoOpStartAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoOpStartParser(mKoOpStartAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mOpEndServiceId) {
			try {
				mKoOpEndAdapter = new KoOpEndAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoOpEndParser(mKoOpEndAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mGetOpStartedListServiceId) {
			try {
				mKoGetOpStartedListAdapter = new KoGetOpStartedListAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoGetOpStartedListParser(mKoGetOpStartedListAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mGetOpMoveAllListServiceId) {
			try {
				mKoGetOpMoveAllListAdapter = new KoGetOpStartedListAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoGetOpStartedListParser(mKoGetOpMoveAllListAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mQaListNeedFillListServiceId) {
			try {
				mKoQaListNeedFillAdapter = new KoQaListNeedFillAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoQaListNeedFillParser(mKoQaListNeedFillAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mQaListByPlanIdListServiceId) {
			try {
				mKoQaListByPlanIdAdapter = new KoQaListNeedFillAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoQaListNeedFillParser(mKoQaListByPlanIdAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mQaListManualAddListServiceId) {
			try {
				mKoQaListManualAdapter = new KoQaListNeedFillAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoQaListNeedFillParser(mKoQaListManualAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mQaInsertDataServiceId) {
			try {
				mKoQaInsertDataAdapter = new KoQaInsertDataAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoQaInsertDataParser(mKoQaInsertDataAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
        }
		else if(id == mGetMsgListServiceId) {
			try {
				mKoPushMsgListAdapter = new KoPushMsgAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoPushMsgListParser(mKoPushMsgListAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mUpdateApkServiceId) {
			try {
				mKoUpdateApkAdapter = new KoUpdateApkAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoUpdateApkParser(mKoUpdateApkAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(id == mOpCheckServiceId) {
			try {
				mKoOpCheckBeforeStartAdapter = new KoOpCheckBeforeStartAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoOpCheckBeforeStartParser(mKoOpCheckBeforeStartAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
        }
        else if(id == mOpDeleteServiceId) {
        	try {
				mKoOpDeleteOpAdapter = new KoOpDeleteOpAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoOpDeleteOpParser(mKoOpDeleteOpAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
        }
        else if(id == mGetCheckItemListServiceId) {
        	try {
				mKoAssetCheckCheckItemAdapter = new KoAssetCheckCheckItemAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoAssetCheckCheckItemParser(mKoAssetCheckCheckItemAdapter));
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
        }
        else if(id == mAssetCheckGetAssetInfoServiceId) {
        	try {
        		mKoAssetCheckGetAssetInfoAdapter = new KoAssetGetAssetInfoAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoAssetGetAssetInfoParser(mKoAssetCheckGetAssetInfoAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
        }
        else if(id == mAssetCheckSubmitCheckServiceId) {
        	try {
        		mKoAssetCheckSubmitCheckAdapter = new KoAssetCheckSubmitCheckAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoAssetCheckSubmitCheckParser(mKoAssetCheckSubmitCheckAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
        }
        else if(id == mAssetCheckGetAssetListServiceId) {
        	try {
        		mKoAssetCheckAssetItemAdapter = new KoAssetCheckAssetItemAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoAssetCheckAssetItemParser(mKoAssetCheckAssetItemAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
        }
        else if(id == mAssetCheckGetSetAssetListServiceId) {
        	try {
        		mKoAssetCheckSetAssetCheckAdapter = new KoAssetCheckSetAssetCheckAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoAssetCheckSetAssetCheckItemParser(mKoAssetCheckSetAssetCheckAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
        }
        else if(id == mAssetCheckUpdateSetAssetListServiceId) {
        	try {
        		mKoAssetCheckUpdateSetAssetCheckAdapter = new KoAssetCheckUpdateSetAssetCheckAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoAssetCheckUpdateSetAssetCheckParser(mKoAssetCheckUpdateSetAssetCheckAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
        }
        else if(id == mAssetCheckGetChangedPartsListServiceId) {
        	try {
        		mKoAssetCheckChangedPartsListAdapter = new KoAssetCheckChangedPartsListAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoAssetCheckChangedPartsListParser(mKoAssetCheckChangedPartsListAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
        }
        else if(id == mAssetCheckCancelCheckAssetServiceId) {
        	try {
        		mKoAssetCheckCancelCheckAssetAdapter = new KoAssetCheckCancelCheckAssetAdapter();
				Xml.parse(is, Encoding.UTF_8, new KoCheckAssetCancelCheckAssetParser(mKoAssetCheckCancelCheckAssetAdapter));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
        }
		
		return OnDataParseListener.SUCCESS;
	}

	
}
