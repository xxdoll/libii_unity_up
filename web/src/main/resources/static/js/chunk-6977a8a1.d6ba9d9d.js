(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-6977a8a1"],{9545:function(e,t,a){},9915:function(e,t,a){"use strict";var i=a("9545"),n=a.n(i);n.a},d33e:function(e,t,a){"use strict";a.r(t);var i=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"device-manage"},[a("div",{staticClass:"section-wrapper"},[a("div",{staticClass:"table-controller"},[a("div",{staticClass:"controller-list"},[a("div",{staticClass:"controller-item"},[a("span",{staticClass:"controller-label"},[e._v("设备ID：")]),a("Input",{staticClass:"controller-input",attrs:{placeholder:"请输入设备ID"},on:{"on-change":e.onInputParams},model:{value:e.deviceId,callback:function(t){e.deviceId=t},expression:"deviceId"}})],1)]),a("div",{staticClass:"new-add"},[a("Button",{attrs:{type:"primary"},on:{click:function(t){e.deviceModal=!0}}},[e._v("新增")])],1)]),a("Table",{staticStyle:{margin:"15px 0"},attrs:{loading:e.tableLoading,columns:e.columns,data:e.data},scopedSlots:e._u([{key:"action",fn:function(t){var i=t.row;return[a("Button",{attrs:{type:"error"},on:{click:function(t){return e._delete(i)}}},[e._v("删除")])]}}])}),a("div",{staticClass:"page"},[a("Page",{attrs:{total:e.total,"show-sizer":"","show-total":"","show-elevator":"",current:e.page,"page-size":e.pageSize},on:{"on-change":e.changePage,"on-page-size-change":e.changePageSize}})],1)],1),a("Modal",{attrs:{title:"新增设备"},on:{"on-visible-change":e.closeModal},model:{value:e.deviceModal,callback:function(t){e.deviceModal=t},expression:"deviceModal"}},[a("Form",{ref:"form",attrs:{model:e.form,rules:e.ruleValidate,"label-width":80}},[a("FormItem",{attrs:{label:"设备ID",prop:"deviceId"}},[a("Input",{attrs:{placeholder:"请输入项目ID"},model:{value:e.form.deviceId,callback:function(t){e.$set(e.form,"deviceId",t)},expression:"form.deviceId"}})],1),a("FormItem",{attrs:{label:"描述",prop:"description"}},[a("Input",{attrs:{placeholder:"请输入描述"},model:{value:e.form.description,callback:function(t){e.$set(e.form,"description",t)},expression:"form.description"}})],1)],1),a("template",{slot:"footer"},[a("Button",{on:{click:function(t){e.deviceModal=!1}}},[e._v("取消")]),a("Button",{attrs:{type:"success",loading:e.loading},on:{click:e.save}},[e._v("确定")])],1)],2)],1)},n=[],o=a("8b55"),c=a("aa98"),s={data:function(){return{deviceId:"",tableLoading:!1,data:[],columns:[{title:"设备ID",key:"deviceId",align:"center"},{title:"描述",key:"description",align:"center"},{title:"操作",slot:"action",align:"center"}],page:1,pageSize:10,total:0,deviceModal:!1,loading:!1,form:{deviceId:"",description:""},ruleValidate:{deviceId:[{required:!0,message:"请输入设备ID",trigger:"blur"}]}}},mounted:function(){this.getData()},methods:{getData:function(){var e=this;this.tableLoading=!0,Object(c["d"])({page:this.page,size:this.pageSize,deviceId:this.deviceId}).then(function(t){var a=t.list,i=t.total;e.tableLoading=!1,e.data=a,e.total=i}).catch(function(){e.tableLoading=!1})},onInputParams:Object(o["a"])(function(){this.getData()},600),changePage:function(e){this.page=e,this.getData()},changePageSize:function(e){this.pageSize=e,this.getData()},_delete:function(e){var t=this;this.$Modal.confirm({title:"删除设备",content:"<p>删除此设备后，相关白名单也会被删除，确认删除此设备？</p>",onOk:function(){Object(c["b"])(e.id).then(function(){t.$Message.success("删除成功"),t.getData()})}})},save:function(){var e=this;this.$refs.form.validate(function(t){t&&(e.loading=!0,Object(c["a"])(e.form).then(function(){e.$Message.success("新增成功"),e.page=1,e.getData(),e.loading=!1,e.deviceModal=!1}).catch(function(){e.loading=!1}))})},closeModal:function(e){e&&this.$refs.form.resetFields()}}},l=s,r=(a("9915"),a("2877")),d=Object(r["a"])(l,i,n,!1,null,"89938f4a",null);t["default"]=d.exports}}]);
//# sourceMappingURL=chunk-6977a8a1.d6ba9d9d.js.map