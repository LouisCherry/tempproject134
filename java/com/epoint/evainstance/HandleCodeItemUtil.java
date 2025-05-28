package com.epoint.evainstance;

public class HandleCodeItemUtil
{
  public String serviceObject(Integer integer)
  {
    String code = "";
    switch (integer.intValue())
    {
    case 10:
      code = "2";
      break;
    case 20:
      code = "1";
      break;
    default:
      code = "1";
    }

    return code;
  }

  public String applyerCertType(String tip)
  {
    String code = "";
    switch (tip)
    {
    case "22":
      code = "111";
      break;
    case "16":
      code = "001";
      break;
    case "14":
      code = "099";
      break;
    case "23":
      code = "113";
      break;
    case "24":
      code = "114";
      break;
    case "414":
      code = "414";
      break;
    case "513":
      code = "513";
      break;
    case "517":
      code = "517";
      break;
    case "516":
      code = "516";
      break;
    case "511":
      code = "511";
      break;
    case "553":
      code = "553";
      break;
    default:
      code = "111";
    }

    return code;
  }
}