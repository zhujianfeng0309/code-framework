package com.generate.framework.base;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * @author SONGJIUHUA386
 * @since 2019/7/12
 */
@MappedSuperclass
public abstract class BaseDomain {

    @Column(
            name = "create_time"
    )
    @CreatedDate
    private Date createTime;
    @Column(
            name = "create_by"
    )
    @CreatedBy
    private Long createBy;
    @Column(
            name = "update_time"
    )
    @LastModifiedDate
    private Date updateTime;
    @Column(
            name = "update_by"
    )
    @LastModifiedBy
    private Long updateBy;

    public BaseDomain() {
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateBy() {
        return this.updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

}
