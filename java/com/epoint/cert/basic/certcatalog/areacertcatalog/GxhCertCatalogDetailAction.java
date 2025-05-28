package com.epoint.cert.basic.certcatalog.areacertcatalog;

import com.epoint.cert.basic.certcatalog.areacertcatalog.domain.CertCatalog;
import com.epoint.core.utils.string.StringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

@RestController("gxhcertcatalogeditaction")
@Scope("request")
public class GxhCertCatalogDetailAction extends CertCatalogDetailAction {

    /**
     * 数据表清单实体对象
     */
    private CertCatalog certCatalog = null;

    private String ispush;
    private String pushcerttypeStr;

    @Override
    public void pageLoad() {
        super.pageLoad();
        certCatalog = super.getCertCatalog();
        if(certCatalog != null){
            ispush = certCatalog.get("is_push");
            if(StringUtil.isBlank(ispush)){
                ispush = "0";
            }
            pushcerttypeStr = certCatalog.get("pushcerttype");
        }
    }

    public String getIspush() {
        return ispush;
    }

    public void setIspush(String ispush) {
        this.ispush = ispush;
    }

    public String getPushcerttypeStr() {
        return pushcerttypeStr;
    }

    public void setPushcerttypeStr(String pushcerttypeStr) {
        this.pushcerttypeStr = pushcerttypeStr;
    }
}
