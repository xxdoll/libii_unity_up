(function(e){function t(t){for(var a,o,i=t[0],c=t[1],l=t[2],u=0,d=[];u<i.length;u++)o=i[u],s[o]&&d.push(s[o][0]),s[o]=0;for(a in c)Object.prototype.hasOwnProperty.call(c,a)&&(e[a]=c[a]);f&&f(t);while(d.length)d.shift()();return r.push.apply(r,l||[]),n()}function n(){for(var e,t=0;t<r.length;t++){for(var n=r[t],a=!0,o=1;o<n.length;o++){var i=n[o];0!==s[i]&&(a=!1)}a&&(r.splice(t--,1),e=c(c.s=n[0]))}return e}var a={},o={app:0},s={app:0},r=[];function i(e){return c.p+"js/"+({}[e]||e)+"."+{"chunk-03c94512":"bd88dfdf","chunk-4375f299":"2dacb403","chunk-6534e097":"332387ea","chunk-6977a8a1":"d6ba9d9d"}[e]+".js"}function c(t){if(a[t])return a[t].exports;var n=a[t]={i:t,l:!1,exports:{}};return e[t].call(n.exports,n,n.exports,c),n.l=!0,n.exports}c.e=function(e){var t=[],n={"chunk-03c94512":1,"chunk-4375f299":1,"chunk-6534e097":1,"chunk-6977a8a1":1};o[e]?t.push(o[e]):0!==o[e]&&n[e]&&t.push(o[e]=new Promise(function(t,n){for(var a="css/"+({}[e]||e)+"."+{"chunk-03c94512":"2c49df85","chunk-4375f299":"e25d9118","chunk-6534e097":"94fdc8cf","chunk-6977a8a1":"760a395c"}[e]+".css",s=c.p+a,r=document.getElementsByTagName("link"),i=0;i<r.length;i++){var l=r[i],u=l.getAttribute("data-href")||l.getAttribute("href");if("stylesheet"===l.rel&&(u===a||u===s))return t()}var d=document.getElementsByTagName("style");for(i=0;i<d.length;i++){l=d[i],u=l.getAttribute("data-href");if(u===a||u===s)return t()}var f=document.createElement("link");f.rel="stylesheet",f.type="text/css",f.onload=t,f.onerror=function(t){var a=t&&t.target&&t.target.src||s,r=new Error("Loading CSS chunk "+e+" failed.\n("+a+")");r.code="CSS_CHUNK_LOAD_FAILED",r.request=a,delete o[e],f.parentNode.removeChild(f),n(r)},f.href=s;var p=document.getElementsByTagName("head")[0];p.appendChild(f)}).then(function(){o[e]=0}));var a=s[e];if(0!==a)if(a)t.push(a[2]);else{var r=new Promise(function(t,n){a=s[e]=[t,n]});t.push(a[2]=r);var l,u=document.createElement("script");u.charset="utf-8",u.timeout=120,c.nc&&u.setAttribute("nonce",c.nc),u.src=i(e),l=function(t){u.onerror=u.onload=null,clearTimeout(d);var n=s[e];if(0!==n){if(n){var a=t&&("load"===t.type?"missing":t.type),o=t&&t.target&&t.target.src,r=new Error("Loading chunk "+e+" failed.\n("+a+": "+o+")");r.type=a,r.request=o,n[1](r)}s[e]=void 0}};var d=setTimeout(function(){l({type:"timeout",target:u})},12e4);u.onerror=u.onload=l,document.head.appendChild(u)}return Promise.all(t)},c.m=e,c.c=a,c.d=function(e,t,n){c.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:n})},c.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},c.t=function(e,t){if(1&t&&(e=c(e)),8&t)return e;if(4&t&&"object"===typeof e&&e&&e.__esModule)return e;var n=Object.create(null);if(c.r(n),Object.defineProperty(n,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var a in e)c.d(n,a,function(t){return e[t]}.bind(null,a));return n},c.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return c.d(t,"a",t),t},c.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},c.p="./",c.oe=function(e){throw console.error(e),e};var l=window["webpackJsonp"]=window["webpackJsonp"]||[],u=l.push.bind(l);l.push=t,l=l.slice();for(var d=0;d<l.length;d++)t(l[d]);var f=u;r.push([0,"chunk-vendors"]),n()})({0:function(e,t,n){e.exports=n("56d7")},"0d5e":function(e,t,n){"use strict";n.d(t,"c",function(){return r});var a=n("bc3a"),o=n.n(a),s=o.a.create({baseURL:window.g.baseUrl,withCredentials:!0}),r=function(e){s.interceptors.response.use(function(t){if(200===t.status&&200===t.data.code)return Promise.resolve(t.data.data);if(t.data&&-999===t.data.code)return window.location.href=window.g.loginPageUrl,Promise.reject();var n=t.data?t.data.message:"服务器错误";return e.$Message.error(n),Promise.reject(n)},function(t){if(console.dir(t),"undefined"===typeof t.response&&"Network Error"===t.message)return e.$Message.info("请登录"),window.location.href=window.g.loginPageUrl,Promise.reject(t);var n=t.response,a="服务器错误";return n.data&&n.data.message&&(a=n.data.message),e.$Message.error(a),Promise.reject(t)})};t["b"]=s},"12d4":function(e,t,n){},"24d2":function(e,t,n){"use strict";n.d(t,"e",function(){return o}),n.d(t,"f",function(){return s}),n.d(t,"a",function(){return r}),n.d(t,"d",function(){return i}),n.d(t,"b",function(){return c}),n.d(t,"c",function(){return l});var a=n("0d5e");function o(){return a["b"].get("/project/all")}function s(e){return a["b"].get("/project/list",{params:e})}function r(e){return a["b"].post("/project",e)}function i(e){return a["b"].put("/project",e)}function c(e,t){var n="/project/check";return a["b"].get(n,{params:{domainField:e,value:t}})}function l(e){return a["b"].delete("/project",{params:{id:e}})}},3072:function(e,t,n){"use strict";var a=n("12d4"),o=n.n(a);o.a},4317:function(e,t){e.exports="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEkAAABJCAYAAABxcwvcAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA2ZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMy1jMDExIDY2LjE0NTY2MSwgMjAxMi8wMi8wNi0xNDo1NjoyNyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDowNzM1RTc5RkMyQURFOTExOTg3RUZBNjdCNTYwOEE5RiIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo5MTIyNDFFOUIzNkMxMUU5QkI4QUFDNUI5MzZCMEIxOSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo5MTIyNDFFOEIzNkMxMUU5QkI4QUFDNUI5MzZCMEIxOSIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M2IChXaW5kb3dzKSI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOkU3QzQ2NDgzNjhCM0U5MTFCMzE2QTM2NjZEODQ0MzMzIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjA3MzVFNzlGQzJBREU5MTE5ODdFRkE2N0I1NjA4QTlGIi8+IDwvcmRmOkRlc2NyaXB0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"},"56d7":function(e,t,n){"use strict";n.r(t);var a={};n.r(a),n.d(a,"actionA",function(){return Z}),n.d(a,"actionB",function(){return X});var o={};n.r(o),n.d(o,"route",function(){return V}),n.d(o,"routePath",function(){return W}),n.d(o,"user",function(){return H});n("cadf"),n("551c"),n("f751"),n("097d");var s=n("2b0e"),r=n("e069"),i=n.n(r),c=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"layout",attrs:{id:"app"}},[a("Layout",[a("Header",{staticClass:"header"},[a("div",{staticClass:"layout-logo"},[a("a",{staticClass:"layout-logo-img",attrs:{href:e.collectionUrl}},[a("img",{attrs:{src:n("cf05"),alt:"利比科技"}})])]),a("user-info")],1),a("Layout",{style:{minHeight:"100vh"}},[a("Sider",{staticClass:"slider",style:{background:"#515a6e"},attrs:{"collapsed-width":78},model:{value:e.isCollapsed,callback:function(t){e.isCollapsed=t},expression:"isCollapsed"}},[a("Menu",{class:e.menuitemClasses,attrs:{theme:"dark","active-name":e.activeTab,width:"auto"}},e._l(e.menus,function(t){return a("MenuItem",{key:t.name,attrs:{to:t.url,name:t.url}},[a("Icon",{attrs:{type:t.icon}}),a("span",[e._v(e._s(t.title))])],1)}),1)],1),a("Layout",{staticClass:"content",class:{collapse:e.isCollapsed}},[a("Breadcrumb",{staticClass:"breadcrumb-wrapper"},e._l(e.breadCrumbs,function(t){return a("BreadcrumbItem",{key:t.url,attrs:{to:t.url}},[e._v(e._s(t.title))])}),1),a("Content",{staticClass:"content-wrapper"},[a("router-view",{attrs:{isCollapsed:e.isCollapsed},on:{expand:e.expandSlider}})],1)],1)],1)],1)],1)},l=[],u=(n("ac6a"),n("28a5"),n("cebc")),d=n("0d5e"),f=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",{staticClass:"user-info"},[a("div",{staticClass:"user-name",on:{click:e.toggleActions}},[a("img",{staticClass:"user-avatar",class:{"with-shadow":e.showActions},staticStyle:{width:"44px",height:"44px","margin-right":"10px"},attrs:{src:n("4317"),alt:"头像"}}),a("span",{staticClass:"username"},[e._v(e._s(e.user.username))])]),a("ul",{directives:[{name:"show",rawName:"v-show",value:e.showActions,expression:"showActions"}],staticClass:"user-actions",class:{"without-password":!e.showChangePassword}},[a("li",{staticClass:"user-action-item",on:{click:function(t){e.showUserChangeModal=!0}}},[a("Icon",{attrs:{type:"md-person"}}),a("span",[e._v("个人信息")])],1),e.showChangePassword?a("li",{staticClass:"user-action-item",on:{click:function(t){e.showChangepasswordModal=!0}}},[a("Icon",{attrs:{type:"md-key"}}),a("span",[e._v("密码修改")])],1):e._e(),a("li",{staticClass:"user-action-item",on:{click:e.__logOut}},[a("Icon",{attrs:{type:"md-log-out"}}),a("span",[e._v("退出登录")])],1)]),a("Modal",{staticClass:"platform-modal user-info-modal",model:{value:e.showUserChangeModal,callback:function(t){e.showUserChangeModal=t},expression:"showUserChangeModal"}},[a("div",{staticClass:"user-change-info"},[a("h3",{staticClass:"user-info-header-title"},[e._v("个人信息")]),a("div",{staticClass:"user-info"},[a("span",{staticClass:"label"},[e._v("账 号")]),a("span",[e._v("：")]),a("span",{staticClass:"value"},[e._v(e._s(e.user.username))])]),a("div",{staticClass:"user-info"},[a("span",{staticClass:"label"},[e._v("姓 名")]),a("span",[e._v("：")]),a("span",{staticClass:"value"},[e._v(e._s(e.user.actualName))])]),a("div",{staticClass:"user-info"},[a("span",{staticClass:"label"},[e._v("职 位")]),a("span",[e._v("：")]),a("span",{staticClass:"value"},[e._v(e._s(e.user.postName))])]),a("div",{staticClass:"user-info"},[a("span",{staticClass:"label"},[e._v("手机号码")]),a("span",[e._v("：")]),a("span",{staticClass:"value"},[e._v(e._s(e.user.phone))])]),a("div",{staticClass:"user-info"},[a("span",{staticClass:"label"},[e._v("Email")]),a("span",[e._v("：")]),a("span",{staticClass:"value"},[e._v(e._s(e.user.email))])])]),a("div",{staticClass:"footer",attrs:{slot:"footer"},slot:"footer"})]),a("Modal",{staticClass:"platform-modal user-info-modal",model:{value:e.showChangepasswordModal,callback:function(t){e.showChangepasswordModal=t},expression:"showChangepasswordModal"}},[a("div",{staticClass:"user-change-info"},[a("h3",{staticClass:"user-info-header-title"},[e._v("修改密码")]),a("Form",{ref:"changePasswordForm",staticClass:"change-password-form",attrs:{model:e.changePasswordInput,rules:e.passwordRules}},[a("FormItem",{attrs:{prop:"password",label:"现密码"}},[a("Input",{attrs:{type:"password"},model:{value:e.changePasswordInput.password,callback:function(t){e.$set(e.changePasswordInput,"password",t)},expression:"changePasswordInput.password"}})],1),a("FormItem",{attrs:{prop:"newPassword",label:"新密码"}},[a("Input",{attrs:{type:"password"},model:{value:e.changePasswordInput.newPassword,callback:function(t){e.$set(e.changePasswordInput,"newPassword",t)},expression:"changePasswordInput.newPassword"}})],1),a("FormItem",{attrs:{prop:"confirmPassword",label:"确认新密码"}},[a("Input",{attrs:{type:"password"},model:{value:e.changePasswordInput.confirmPassword,callback:function(t){e.$set(e.changePasswordInput,"confirmPassword",t)},expression:"changePasswordInput.confirmPassword"}})],1)],1),a("div",{staticClass:"buttons",staticStyle:{"margin-top":"10px"}},[a("Button",{attrs:{loading:e.isChangingPassword,type:"primary"},on:{click:e.changePwd}},[e._v("修改密码")])],1)],1),a("div",{staticClass:"footer",attrs:{slot:"footer"},slot:"footer"})])],1)},p=[],g=n("2f62");function m(e){var t="user/updatePwd";return d["b"].post(t,e)}var h={props:{showChangePassword:{type:Boolean,default:!1}},data:function(){var e=this,t=function(t,n,a){n!==e.changePasswordInput.newPassword?a(new Error("新密码与确认密码不一致")):a()};return{showActions:!1,showUserChangeModal:!1,showChangepasswordModal:!1,changePasswordInput:{password:"",newPassword:"",confirmPassword:""},passwordRules:{password:[{required:!0,message:"现密码不能为空",trigger:"blur"}],newPassword:[{required:!0,message:"新密码不能为空",trigger:"blur"},{type:"string",min:6,message:"密码必须大于等于6位",trigger:"blur"}],confirmPassword:[{required:!0,message:"确认密码不能为空",trigger:"blur"},{validator:t,trigger:"blur"}]},isChangingPassword:!1}},mounted:function(){},methods:Object(u["a"])({__logOut:function(){window.location.href="".concat(window.g.baseUrl,"/logout")}},Object(g["c"])({setUser:"SET_USER"}),{toggleActions:function(){this.showActions=!this.showActions},closeUserChangeModal:function(){this.showUserChangeModal=!1},changePwd:function(){var e=this;this.$refs.changePasswordForm.validate(function(t){t&&(e.isChangingPassword=!0,m(e.changePasswordInput).then(function(){e.$Message.success("修改密码成功"),e.isChangingPassword=!1,e.showChangepasswordModal=!1}).catch(function(){e.isChangingPassword=!1}))})}}),computed:Object(u["a"])({},Object(g["b"])(["userMenus","user"]))},v=h,b=(n("3072"),n("2877")),w=Object(b["a"])(v,f,p,!1,null,null,null),C=w.exports,y=n("24d2"),k=n("aa98"),I=n("71ff"),P={components:{UserInfo:C},data:function(){return{collectionUrl:window.g.collectionUrl,isCollapsed:!1,menus:[{url:"/",icon:"ios-cog",name:"1-1",title:"资源管理"},{url:"/project",icon:"ios-keypad",name:"2-1",title:"项目管理"},{url:"/white-list",icon:"ios-person-add-outline",name:"3-1",title:"白名单管理"},{url:"/device-manage",icon:"md-laptop",name:"4-1",title:"设备管理"},{url:"/hot-update",icon:"md-flame",name:"5-1",title:"热更新资源管理"}],activeMenuItem:"/"}},created:function(){Object(d["c"])(this)},mounted:function(){this.getData()},methods:Object(u["a"])({collapseSlider:function(){this.isCollapsed=!0},expandSlider:function(){this.isCollapsed=!1},getData:function(){var e=this;Object(y["e"])().then(function(t){e.setProject(t)}).catch(function(){}),Object(k["c"])().then(function(t){e.setDevice(t)}),Object(I["d"])().then(function(t){e.setHot(t)})}},Object(g["c"])({setProject:"SET_PROJECT",setDevice:"SET_DEVICE",setHot:"SET_HOT_SEARCH"})),computed:{menuitemClasses:function(){return["menu-item",this.isCollapsed?"collapsed-menu":""]},activeTab:function(){var e=this.$route.path.split("/"),t=e[1];return"/".concat(t)},breadCrumbs:function(){var e=this.$route,t=e.meta,n=e.fullPath,a={title:t.title,url:n};if(t.parents){var o=[];return t.parents.forEach(function(e){o.push({title:e.title,url:e.path})}),o.push(a),o}return[a]}}},E=P,A=(n("7c55"),Object(b["a"])(E,c,l,!1,null,null,null)),M=A.exports,S=n("8c4f"),j=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"home"},[n("div",{staticClass:"section-wrapper"},[n("div",{staticClass:"table-controller"},[n("div",{staticClass:"controller-list"},[n("div",{staticClass:"controller-item"},[n("span",{staticClass:"controller-label"},[e._v("项目ID：")]),n("Input",{staticClass:"controller-input",attrs:{placeholder:"项目编码"},on:{"on-change":e.onInputParams},model:{value:e.filters.code,callback:function(t){e.$set(e.filters,"code",t)},expression:"filters.code"}})],1),n("div",{staticClass:"controller-item"},[n("span",{staticClass:"controller-label"},[e._v("版本号：")]),n("Input",{staticClass:"controller-input",attrs:{placeholder:"版本号"},on:{"on-change":e.onInputParams},model:{value:e.filters.version,callback:function(t){e.$set(e.filters,"version",t)},expression:"filters.version"}})],1),n("div",{staticClass:"controller-item"},[n("span",{staticClass:"controller-label"},[e._v("状态：")]),n("Select",{staticClass:"controller-input",on:{"on-change":e.getData},model:{value:e.filters.status,callback:function(t){e.$set(e.filters,"status",t)},expression:"filters.status"}},[n("Option",{attrs:{value:"all"}},[e._v("全部")]),n("Option",{attrs:{value:1}},[e._v("测试")]),n("Option",{attrs:{value:2}},[e._v("正式")])],1)],1)]),n("div",{staticClass:"btn-group"},[n("Button",{staticStyle:{"margin-right":"8px"},attrs:{type:"primary"},on:{click:function(t){e.configModal=!0}}},[e._v("上传配置文件")]),n("Button",{attrs:{type:"primary"},on:{click:function(t){e.resourceModal=!0}}},[e._v("上传资源")])],1)]),n("Table",{staticStyle:{margin:"15px 0"},attrs:{loading:e.tableLoading,columns:e.columns,data:e.data},scopedSlots:e._u([{key:"action",fn:function(t){var a=t.row;return[n("Button",{attrs:{type:"error"},on:{click:function(t){return e._deleteResources(a)}}},[e._v("删除")])]}}])}),n("div",{staticClass:"page"},[n("Page",{attrs:{total:e.total,"show-sizer":"","show-total":"","show-elevator":"",current:e.page,"page-size":e.pageSize},on:{"on-change":e.changePage,"on-page-size-change":e.changePageSize}})],1)],1),n("Modal",{attrs:{title:"上传配置文件"},on:{"on-visible-change":e.closeConfigModal},model:{value:e.configModal,callback:function(t){e.configModal=t},expression:"configModal"}},[n("Form",{ref:"configForm",attrs:{model:e.configForm,rules:e.configRuleValidate,"label-width":120}},[n("FormItem",{attrs:{label:"项目编码",prop:"code"}},[n("Select",{model:{value:e.configForm.code,callback:function(t){e.$set(e.configForm,"code",t)},expression:"configForm.code"}},e._l(e.projectList,function(t){return n("Option",{key:t.id,attrs:{value:t.code}},[e._v(e._s(t.code))])}),1)],1),n("FormItem",{attrs:{label:"上传配置文件",prop:"configFileName"}},[n("Upload",{attrs:{action:".","before-upload":e.onConfigFileSelected,type:"drag"}},[e.configForm.configFileName?e._e():n("div",{staticStyle:{padding:"20px 0"}},[n("Icon",{staticStyle:{color:"#3399ff"},attrs:{type:"ios-cloud-upload",size:"52"}}),n("p",[e._v("拖动或者点击上传配置文件")])],1),e.configForm.configFileName?n("div",[e._v("\n            "+e._s(e.configForm.configFileName)+"\n          ")]):e._e()])],1)],1),n("template",{slot:"footer"},[n("Button",{on:{click:function(t){e.configModal=!1}}},[e._v("取消")]),n("Button",{attrs:{type:"success",loading:e.configLoading},on:{click:e.saveConfig}},[e._v("确定")])],1)],2),n("Modal",{attrs:{title:"上传资源"},on:{"on-visible-change":e.closeModal},model:{value:e.resourceModal,callback:function(t){e.resourceModal=t},expression:"resourceModal"}},[n("Form",{ref:"form",attrs:{model:e.form,rules:e.ruleValidate,"label-width":80}},[n("FormItem",{attrs:{label:"项目ID",prop:"code"}},[n("Select",{on:{"on-change":e.changeCode},model:{value:e.form.code,callback:function(t){e.$set(e.form,"code",t)},expression:"form.code"}},e._l(e.projectList,function(t){return n("Option",{key:t.id,attrs:{value:t.code}},[e._v(e._s(t.code))])}),1)],1),n("FormItem",{attrs:{label:"项目名称",prop:"name"}},[n("Select",{on:{"on-change":e.changeName},model:{value:e.form.name,callback:function(t){e.$set(e.form,"name",t)},expression:"form.name"}},e._l(e.projectList,function(t){return n("Option",{key:t.id,attrs:{value:t.code}},[e._v(e._s(t.name))])}),1)],1),n("FormItem",{attrs:{label:"状态",prop:"status"}},[n("RadioGroup",{model:{value:e.form.status,callback:function(t){e.$set(e.form,"status",t)},expression:"form.status"}},[n("Radio",{attrs:{label:1}},[e._v("测试")]),n("Radio",{attrs:{label:2}},[e._v("正式")])],1)],1),n("FormItem",{attrs:{label:"上传资源",prop:"fileName"}},[n("Upload",{attrs:{action:".","before-upload":e.onFileSelected,type:"drag"}},[e.form.fileName?e._e():n("div",{staticStyle:{padding:"20px 0"}},[n("Icon",{staticStyle:{color:"#3399ff"},attrs:{type:"ios-cloud-upload",size:"52"}}),n("p",[e._v("拖动或者点击上传资源包")])],1),e.form.fileName?n("div",[e._v("\n            "+e._s(e.form.fileName)+"\n          ")]):e._e()])],1)],1),n("template",{slot:"footer"},[n("Button",{on:{click:function(t){e.resourceModal=!1}}},[e._v("取消")]),n("Button",{attrs:{type:"success",loading:e.loading},on:{click:e.save}},[e._v("确定")])],1)],2)],1)},F=[],U=(n("7f7f"),n("8b55"));n("456d");function N(e){return d["b"].get("/unity/list",{params:e})}function O(e){var t=new FormData;return Object.keys(e).forEach(function(n){t.append(n,e[n])}),d["b"].post("/unity/upload",t,{headers:{"Content-Type":"multipart/form-data"}})}function D(e){var t=new FormData;return Object.keys(e).forEach(function(n){t.append(n,e[n])}),d["b"].post("/unity/config",t,{headers:{"Content-Type":"multipart/form-data"}})}function x(e){return d["b"].delete("/unity",{params:{id:e}})}var _={1:"测试",2:"正式"},T={data:function(){return{tableLoading:!1,data:[],columns:[{title:"项目ID",key:"code",align:"center"},{title:"版本号",key:"version",align:"center"},{title:"测试服路径",key:"localPath",align:"center",render:function(e,t){return e("a",{attrs:{href:t.row.localPath}},t.row.localPath)}},{title:"cdn路径",key:"cdnPath",align:"center",render:function(e,t){return e("a",{attrs:{href:t.row.cdnPath}},t.row.cdnPath)}},{title:"状态",key:"status",align:"center",render:function(e,t){return e("span",_[t.row.status])}},{title:"时间",key:"createTime",align:"center"},{title:"操作",slot:"action",align:"center"}],filters:{code:"",status:"all",version:""},page:1,pageSize:10,total:1,resourceModal:!1,loading:!1,form:{code:"",name:"",status:1,fileName:""},ruleValidate:{code:[{required:!0,message:"请选择项目ID",trigger:"change"}],name:[{required:!0,message:"请选择项目名称",trigger:"change"}],status:[{required:!0,message:"请选择状态"}],fileName:[{required:!0,message:"请选择上传资源"}]},configModal:!1,configLoading:!1,configForm:{configFileName:"",code:""},configRuleValidate:{code:[{required:!0,message:"请选择项目编码",trigger:"change"}],fileName:[{required:!0,message:"请选择上传配置文件"}]}}},mounted:function(){this.getData()},methods:{getData:function(){var e=this,t={code:this.filters.code,status:"all"===this.filters.status?"":this.filters.status,version:this.filters.version,page:this.page,size:this.pageSize};this.tableLoading=!0,N(t).then(function(t){var n=t.list,a=t.total;e.data=n,e.total=a,e.tableLoading=!1}).catch(function(){e.tableLoading=!1})},onInputParams:Object(U["a"])(function(){this.getData()},600),_deleteResources:function(e){var t=this;this.$Modal.confirm({title:"删除资源",content:"<p>确认删除此资源？</p>",onOk:function(){x(e.id).then(function(){t.$Message.success("删除成功"),t.getData()}).catch(function(){})}})},changePage:function(e){this.page=e,this.getData()},changePageSize:function(e){this.pageSize=e,this.getData()},closeModal:function(e){e&&this.$refs.form.resetFields()},onFileSelected:function(e){return this.form.zipFile=e,this.form.fileName=e.name,!1},save:function(){var e=this;this.$refs.form.validate(function(t){if(t){var n={code:e.form.code,name:e.codeNameMap[e.form.name],zipFile:e.form.zipFile,status:e.form.status};e.loading=!0,O(n).then(function(){e.$Message.success("上传成功"),e.getData(),e.loading=!1,e.resourceModal=!1}).catch(function(){e.loading=!1,e.resourceModal=!1})}})},changeCode:function(e){this.form.name=e},changeName:function(e){this.form.code=e},closeConfigModal:function(e){e&&this.$refs.configForm.resetFields()},onConfigFileSelected:function(e){return this.configForm.configFile=e,this.configForm.configFileName=e.name,!1},saveConfig:function(){var e=this;this.$refs.configForm.validate(function(t){t&&(e.configLoading=!0,D(e.configForm).then(function(){e.$Message.success("上传成功"),e.getData(),e.configLoading=!1,e.configModal=!1}).catch(function(){e.configLoading=!1,e.configModal=!1}))})}},computed:Object(u["a"])({},Object(g["d"])(["projectList","codeNameMap"]))},R=T,L=(n("e982"),Object(b["a"])(R,j,F,!1,null,"b92194f0",null)),B=L.exports;s["default"].use(S["a"]);var Y,z=new S["a"]({base:"./",routes:[{path:"/",name:"home",component:B,meta:{title:"资源管理"}},{path:"/project",name:"project",component:function(){return n.e("chunk-03c94512").then(n.bind(null,"07bd"))},meta:{title:"项目管理"}},{path:"/white-list",name:"whiteList",component:function(){return n.e("chunk-4375f299").then(n.bind(null,"fc19"))},meta:{title:"白名单管理"}},{path:"/device-manage",name:"deviceMange",component:function(){return n.e("chunk-6977a8a1").then(n.bind(null,"d33e"))},meta:{title:"设备管理"}},{path:"/hot-update",name:"hotUpdate",component:function(){return n.e("chunk-6534e097").then(n.bind(null,"f6e6"))},meta:{title:"热更新资源管理"}}]}),G=z,J=n("b054"),Q=n.n(J),Z=function(){},X=function(){},V=function(e){return e.route},W=function(e){return e.routePath},H=function(e){return e.user},K={route:{},routePath:"",user:{},projectList:[],codeNameMap:{},deviceList:[],gameList:[],searchTree:{}},q=K,$=n("bd86"),ee="SET_ROUTE",te="SET_ROUTE_PATH",ne="SET_USER",ae="SET_PROJECT",oe="SET_DEVICE",se="SET_HOT_SEARCH",re=(Y={},Object($["a"])(Y,ee,function(e,t){e.route=t}),Object($["a"])(Y,te,function(e,t){e.routePath=t}),Object($["a"])(Y,ne,function(e,t){e.user=t}),Object($["a"])(Y,ae,function(e,t){e.projectList=t;var n={};t.forEach(function(e){n[e.code]=e.name}),e.codeNameMap=n}),Object($["a"])(Y,oe,function(e,t){e.deviceList=t}),Object($["a"])(Y,se,function(e,t){var n=Object.keys(t);e.gameList=n,e.searchTree=t}),Y),ie=re;s["default"].use(g["a"]);var ce=!1,le=new g["a"].Store({actions:a,getters:o,state:q,mutations:ie,strict:ce,plugins:ce?[Q()()]:[]});n("693a");s["default"].use(i.a),s["default"].prototype.$axios=d["b"],s["default"].prototype.$get=d["axiosGet"],s["default"].config.productionTip=!1;new s["default"]({router:G,store:le,render:function(e){return e(M)}}).$mount("#app")},"5c48":function(e,t,n){},"693a":function(e,t,n){},"71ff":function(e,t,n){"use strict";n.d(t,"d",function(){return o}),n.d(t,"e",function(){return s}),n.d(t,"c",function(){return r}),n.d(t,"b",function(){return i}),n.d(t,"a",function(){return c});var a=n("0d5e");function o(){return a["b"].get("/hot-update/tree")}function s(e){return a["b"].get("/hot-update/list",{params:e})}function r(e){return a["b"].get("/hot-update/app-version",{params:e})}function i(e){return a["b"].put("/hot-update",e)}function c(e){return a["b"].delete("/hot-update",{params:{id:e}})}},"7c55":function(e,t,n){"use strict";var a=n("5c48"),o=n.n(a);o.a},"82f9":function(e,t,n){},"8b55":function(e,t,n){"use strict";n.d(t,"a",function(){return a});n("456d"),n("ac6a"),n("a481");function a(e,t){var n;return function(){var a=this,o=arguments;clearTimeout(n),n=setTimeout(function(){e.apply(a,o)},t)}}},aa98:function(e,t,n){"use strict";n.d(t,"c",function(){return o}),n.d(t,"d",function(){return s}),n.d(t,"a",function(){return r}),n.d(t,"b",function(){return i});var a=n("0d5e");function o(){return a["b"].get("/device/all")}function s(e){return a["b"].get("/device/list",{params:e})}function r(e){return a["b"].post("/device",e)}function i(e){return a["b"].delete("/device",{params:{id:e}})}},cf05:function(e,t,n){e.exports=n.p+"img/logo.6f7f1fe1.png"},e982:function(e,t,n){"use strict";var a=n("82f9"),o=n.n(a);o.a}});
//# sourceMappingURL=app.a7c67c0d.js.map