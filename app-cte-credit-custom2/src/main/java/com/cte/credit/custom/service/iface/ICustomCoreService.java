package com.cte.credit.custom.service.iface;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.cte.credit.api.dto.CRSCoreRequest;
import com.cte.credit.api.dto.CRSCoreResponse;

@Service
public  interface ICustomCoreService
{
  public  boolean valid(String paramString, Map<String, Object> paramMap, String[] paramArrayOfString, String[] nullableparam);

  public  CRSCoreResponse handler(String paramString, CRSCoreRequest paramCRSCoreRequest);

  public  Map<String, String> callback(String paramString, CRSCoreRequest paramCRSCoreRequest, CRSCoreResponse paramCRSCoreResponse);
}