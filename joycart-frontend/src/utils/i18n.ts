import {
  DEFAULT_LANGUAGE,
  LANGUAGE_EN_US,
  LANGUAGE_FR_FR,
  LANGUAGE_ZH_CN,
  STORAGE_LANGUAGE
} from "../constants/apiConstants";

/**
 * i18n工具类 - 多语言支持
 * 管理用户语言偏好，提供语言切换功能
 */

export { DEFAULT_LANGUAGE, LANGUAGE_EN_US, LANGUAGE_FR_FR, LANGUAGE_ZH_CN } from "../constants/apiConstants";

// 语言偏好变更事件名称
export const LANGUAGE_CHANGE_EVENT = "joycart:language-change";

interface LanguageChangeDetail {
  language: string;
  previousLanguage: string;
}

export type LanguageChangeHandler = (language: string, previousLanguage: string) => void;

export interface LanguageOption {
  code: string;
  name: string;
  nativeName: string;
}

export const LANGUAGE_OPTIONS: LanguageOption[] = [
  { code: LANGUAGE_EN_US, name: "English", nativeName: "English" },
  { code: LANGUAGE_ZH_CN, name: "Chinese", nativeName: "中文" },
  { code: LANGUAGE_FR_FR, name: "French", nativeName: "Français" }
];

const SUPPORTED_LANGUAGES = [LANGUAGE_EN_US, LANGUAGE_ZH_CN, LANGUAGE_FR_FR];

const UI_TRANSLATION_KEYS_BASE = {
  nav: {
    home: "nav.home",
    category: "nav.category",
    cart: "nav.cart",
    profile: "nav.profile"
  },
  home: {
    newProductTitle: "home.newProduct",
    flashSaleTitle: "home.flashSale",
    bottomPlaceholder: "home.bottomPlaceholder"
  },
  login: {
    phoneNumberLabel: "login.phoneNumberLabel",
    phoneNumberPlaceholder: "login.phoneNumberPlaceholder",
    passwordLabel: "login.passwordLabel",
    passwordPlaceholder: "login.passwordPlaceholder",
    submitButton: "login.submitButton",
    agreePrefix: "login.agreePrefix",
    termsLink: "login.termsLink",
    privacyLink: "login.privacyLink"
  },
  register: {
    usernameLabel: "register.usernameLabel",
    usernamePlaceholder: "register.usernamePlaceholder",
    phoneNumberLabel: "register.phoneNumberLabel",
    phoneNumberPlaceholder: "register.phoneNumberPlaceholder",
    passwordLabel: "register.passwordLabel",
    passwordPlaceholder: "register.passwordPlaceholder",
    confirmPasswordLabel: "register.confirmPasswordLabel",
    confirmPasswordPlaceholder: "register.confirmPasswordPlaceholder",
    languagePreferenceLabel: "register.languagePreferenceLabel",
    submitButton: "register.submitButton",
    agreePrefix: "register.agreePrefix",
    termsLink: "register.termsLink",
    privacyLink: "register.privacyLink"
  },
  messages: {
    phoneRequired: "messages.phoneRequired",
    passwordRequired: "messages.passwordRequired",
    passwordTooShort: "messages.passwordTooShort",
    passwordMismatch: "messages.passwordMismatch",
    usernameRequired: "messages.usernameRequired",
    agreeTerms: "messages.agreeTerms"
  },
  common: {
    quantityLabel: "common.quantityLabel",
    addToCart: "common.addToCart",
    buyNow: "common.buyNow",
    shoppingCart: "common.shoppingCart",
    soldPrefix: "common.soldPrefix",
    buyButton: "common.buyButton",
    cartUpdateSuccess: "common.cartUpdateSuccess",
    cartUpdateFailed: "common.cartUpdateFailed",
    cartAddSuccess: "common.cartAddSuccess",
    cartAddFailed: "common.cartAddFailed",
    selectProductsFirst: "common.selectProductsFirst",
    orderSubmitFailed: "common.orderSubmitFailed",
    logoutSuccess: "common.logoutSuccess",
    logoutFailed: "common.logoutFailed",
    requestFailed: "common.requestFailed",
    bottomLine: "common.bottomLine"
  },
  search: {
    placeholder: "search.placeholder",
    historyTitle: "search.historyTitle",
    hotTitle: "search.hotTitle"
  },
  searchList: {
    placeholder: "searchList.placeholder",
    tabDefault: "searchList.tabDefault",
    tabSale: "searchList.tabSale",
    tabPrice: "searchList.tabPrice",
    sold: "searchList.sold"
  },
  nearby: {
    title: "nearby.title",
    searchPlaceholder: "nearby.searchPlaceholder",
    subtitle: "nearby.subtitle",
    phoneLabel: "nearby.phoneLabel"
  },
  order: {
    loading: "order.loading",
    title: "order.title",
    receiverLabel: "order.receiverLabel",
    addressLabel: "order.addressLabel",
    deliveryTime: "order.deliveryTime",
    totalLabel: "order.totalLabel",
    placeOrderButton: "order.placeOrderButton",
    chooseAddressTitle: "order.chooseAddressTitle",
    choosePaymentTitle: "order.choosePaymentTitle",
    paymentWeChat: "order.paymentWeChat",
    paymentBalance: "order.paymentBalance",
    payNowButton: "order.payNowButton",
    detailFailed: "order.detailFailed",
    addressListFailed: "order.addressListFailed",
    paymentSuccess: "order.paymentSuccess",
    paymentFailed: "order.paymentFailed"
  },
  category: {
    pageTitle: "category.pageTitle",
    searchPlaceholder: "category.searchPlaceholder",
    allProductsTab: "category.allProductsTab",
    allTag: "category.allTag",
    featuredProducts: "category.featuredProducts"
  },
  detail: {
    pageTitle: "detail.pageTitle",
    specTitle: "detail.specTitle",
    originLabel: "detail.originLabel",
    specLabel: "detail.specLabel",
    detailSectionTitle: "detail.detailSectionTitle"
  },
  cart: {
    pageTitle: "cart.pageTitle",
    selectAll: "cart.selectAll",
    total: "cart.total",
    checkout: "cart.checkout"
  },
  profile: {
    pageTitle: "profile.pageTitle",
    logout: "profile.logout",
    member: "profile.member",
    coupons: "profile.coupons",
    rewardPoints: "profile.rewardPoints",
    allOrders: "profile.allOrders",
    pendingPayment: "profile.pendingPayment",
    awaitingShipment: "profile.awaitingShipment",
    awaitingDelivery: "profile.awaitingDelivery",
    returnsRefunds: "profile.returnsRefunds",
    customerService: "profile.customerService",
    settings: "profile.settings",
    address: "profile.address"
  }
} as const;

export const UI_TRANSLATION_KEYS = UI_TRANSLATION_KEYS_BASE;

type UiTranslationGroups = typeof UI_TRANSLATION_KEYS_BASE;
type UiTranslationGroup = UiTranslationGroups[keyof UiTranslationGroups];
type UiTranslationKey = UiTranslationGroup[keyof UiTranslationGroup];

const UI_TRANSLATIONS: Record<string, Record<string, string>> = {
  [LANGUAGE_EN_US]: {
    [UI_TRANSLATION_KEYS.nav.home]: "Home",
    [UI_TRANSLATION_KEYS.nav.category]: "Category",
    [UI_TRANSLATION_KEYS.nav.cart]: "Cart",
    [UI_TRANSLATION_KEYS.nav.profile]: "Profile",
    [UI_TRANSLATION_KEYS.home.newProductTitle]: "New Product",
    [UI_TRANSLATION_KEYS.home.flashSaleTitle]: "Flash Sale",
    [UI_TRANSLATION_KEYS.home.bottomPlaceholder]: "-- I am the bottom line --",
    [UI_TRANSLATION_KEYS.login.phoneNumberLabel]: "phone number",
    [UI_TRANSLATION_KEYS.login.phoneNumberPlaceholder]: "please input phone number",
    [UI_TRANSLATION_KEYS.login.passwordLabel]: "password",
    [UI_TRANSLATION_KEYS.login.passwordPlaceholder]: "please input password",
    [UI_TRANSLATION_KEYS.login.submitButton]: "login",
    [UI_TRANSLATION_KEYS.login.agreePrefix]: "I accept the",
    [UI_TRANSLATION_KEYS.login.termsLink]: "Terms and Conditions",
    [UI_TRANSLATION_KEYS.login.privacyLink]: "Privacy Policy",
    [UI_TRANSLATION_KEYS.register.usernameLabel]: "username",
    [UI_TRANSLATION_KEYS.register.usernamePlaceholder]: "please input username",
    [UI_TRANSLATION_KEYS.register.phoneNumberLabel]: "phone number",
    [UI_TRANSLATION_KEYS.register.phoneNumberPlaceholder]: "please input phone number",
    [UI_TRANSLATION_KEYS.register.passwordLabel]: "password",
    [UI_TRANSLATION_KEYS.register.passwordPlaceholder]: "please input password",
    [UI_TRANSLATION_KEYS.register.confirmPasswordLabel]: "confirm password",
    [UI_TRANSLATION_KEYS.register.confirmPasswordPlaceholder]: "please re-input password",
    [UI_TRANSLATION_KEYS.register.languagePreferenceLabel]: "Language Preference",
    [UI_TRANSLATION_KEYS.register.submitButton]: "register",
    [UI_TRANSLATION_KEYS.register.agreePrefix]: "I accept the",
    [UI_TRANSLATION_KEYS.register.termsLink]: "Terms and Conditions",
    [UI_TRANSLATION_KEYS.register.privacyLink]: "Privacy Policy",
    [UI_TRANSLATION_KEYS.messages.phoneRequired]: "phone number should not be empty.",
    [UI_TRANSLATION_KEYS.messages.passwordRequired]: "password should not be empty.",
    [UI_TRANSLATION_KEYS.messages.passwordTooShort]: "password length should not be less than 6.",
    [UI_TRANSLATION_KEYS.messages.passwordMismatch]: "password should be same as checkPassword.",
    [UI_TRANSLATION_KEYS.messages.usernameRequired]: "userName should not be empty.",
    [UI_TRANSLATION_KEYS.messages.agreeTerms]: "Please agree to the Terms and Conditions and Privacy Policy.",
    [UI_TRANSLATION_KEYS.common.quantityLabel]: "Quantity:",
    [UI_TRANSLATION_KEYS.common.addToCart]: "Add to Cart",
    [UI_TRANSLATION_KEYS.common.buyNow]: "Buy Now",
    [UI_TRANSLATION_KEYS.common.shoppingCart]: "Shopping Cart",
    [UI_TRANSLATION_KEYS.common.soldPrefix]: "sold",
    [UI_TRANSLATION_KEYS.common.buyButton]: "buy",
    [UI_TRANSLATION_KEYS.common.cartUpdateSuccess]: "Cart updated successfully!",
    [UI_TRANSLATION_KEYS.common.cartUpdateFailed]: "Cart update failed",
    [UI_TRANSLATION_KEYS.common.cartAddSuccess]: "Product added to cart",
    [UI_TRANSLATION_KEYS.common.cartAddFailed]: "Failed to add product to cart",
    [UI_TRANSLATION_KEYS.common.selectProductsFirst]: "Select at least one product",
    [UI_TRANSLATION_KEYS.common.orderSubmitFailed]: "Order submission failed",
    [UI_TRANSLATION_KEYS.common.logoutSuccess]: "Logged out successfully",
    [UI_TRANSLATION_KEYS.common.logoutFailed]: "Failed to logout",
    [UI_TRANSLATION_KEYS.common.requestFailed]: "Request failed, please try again",
    [UI_TRANSLATION_KEYS.common.bottomLine]: "-- I am the bottom line --",
    [UI_TRANSLATION_KEYS.search.placeholder]: "Please enter product name",
    [UI_TRANSLATION_KEYS.search.historyTitle]: "History Search",
    [UI_TRANSLATION_KEYS.search.hotTitle]: "Hot Search",
    [UI_TRANSLATION_KEYS.searchList.placeholder]: "Please enter product name",
    [UI_TRANSLATION_KEYS.searchList.tabDefault]: "Default",
    [UI_TRANSLATION_KEYS.searchList.tabSale]: "Sale",
    [UI_TRANSLATION_KEYS.searchList.tabPrice]: "Price",
    [UI_TRANSLATION_KEYS.searchList.sold]: "sold",
    [UI_TRANSLATION_KEYS.nearby.title]: "Switch Store",
    [UI_TRANSLATION_KEYS.nearby.searchPlaceholder]: "Please enter address",
    [UI_TRANSLATION_KEYS.nearby.subtitle]: "Nearby Store",
    [UI_TRANSLATION_KEYS.nearby.phoneLabel]: "Tel：",
    [UI_TRANSLATION_KEYS.order.loading]: "Loading...",
    [UI_TRANSLATION_KEYS.order.title]: "Confirm Order",
    [UI_TRANSLATION_KEYS.order.receiverLabel]: "Receiver:",
    [UI_TRANSLATION_KEYS.order.addressLabel]: "Address:",
    [UI_TRANSLATION_KEYS.order.deliveryTime]: "Delivery Time",
    [UI_TRANSLATION_KEYS.order.totalLabel]: "Total:",
    [UI_TRANSLATION_KEYS.order.placeOrderButton]: "Place Order",
    [UI_TRANSLATION_KEYS.order.chooseAddressTitle]: "Choose Address",
    [UI_TRANSLATION_KEYS.order.choosePaymentTitle]: "Choose Payment",
    [UI_TRANSLATION_KEYS.order.paymentWeChat]: "WeChat",
    [UI_TRANSLATION_KEYS.order.paymentBalance]: "Balance",
    [UI_TRANSLATION_KEYS.order.payNowButton]: "Pay Now",
    [UI_TRANSLATION_KEYS.order.detailFailed]: "Failed to retrieve order details",
    [UI_TRANSLATION_KEYS.order.addressListFailed]: "Failed to retrieve address list",
    [UI_TRANSLATION_KEYS.order.paymentSuccess]: "Payment successful!",
    [UI_TRANSLATION_KEYS.order.paymentFailed]: "Payment failed",
    [UI_TRANSLATION_KEYS.category.pageTitle]: "Category",
    [UI_TRANSLATION_KEYS.category.searchPlaceholder]: "Please enter product name",
    [UI_TRANSLATION_KEYS.category.allProductsTab]: "All Products",
    [UI_TRANSLATION_KEYS.category.allTag]: "All",
    [UI_TRANSLATION_KEYS.category.featuredProducts]: "Featured Products",
    [UI_TRANSLATION_KEYS.detail.pageTitle]: "Detail",
    [UI_TRANSLATION_KEYS.detail.specTitle]: "Specification Info",
    [UI_TRANSLATION_KEYS.detail.originLabel]: "origin",
    [UI_TRANSLATION_KEYS.detail.specLabel]: "spec",
    [UI_TRANSLATION_KEYS.detail.detailSectionTitle]: "product detail",
    [UI_TRANSLATION_KEYS.cart.pageTitle]: "Cart",
    [UI_TRANSLATION_KEYS.cart.selectAll]: "Select All",
    [UI_TRANSLATION_KEYS.cart.total]: "Total:",
    [UI_TRANSLATION_KEYS.cart.checkout]: "Checkout",
    [UI_TRANSLATION_KEYS.profile.pageTitle]: "Profile",
    [UI_TRANSLATION_KEYS.profile.logout]: "Logout",
    [UI_TRANSLATION_KEYS.profile.member]: "Member",
    [UI_TRANSLATION_KEYS.profile.coupons]: "Coupons",
    [UI_TRANSLATION_KEYS.profile.rewardPoints]: "Reward Points",
    [UI_TRANSLATION_KEYS.profile.allOrders]: "All Orders",
    [UI_TRANSLATION_KEYS.profile.pendingPayment]: "Pending Payment",
    [UI_TRANSLATION_KEYS.profile.awaitingShipment]: "Awaiting Shipment",
    [UI_TRANSLATION_KEYS.profile.awaitingDelivery]: "Awaiting Delivery",
    [UI_TRANSLATION_KEYS.profile.returnsRefunds]: "Returns & Refunds",
    [UI_TRANSLATION_KEYS.profile.customerService]: "Customer Service",
    [UI_TRANSLATION_KEYS.profile.settings]: "Settings",
    [UI_TRANSLATION_KEYS.profile.address]: "Address"
  },
  [LANGUAGE_ZH_CN]: {
    [UI_TRANSLATION_KEYS.nav.home]: "首页",
    [UI_TRANSLATION_KEYS.nav.category]: "分类",
    [UI_TRANSLATION_KEYS.nav.cart]: "购物车",
    [UI_TRANSLATION_KEYS.nav.profile]: "我的",
    [UI_TRANSLATION_KEYS.home.newProductTitle]: "新品推荐",
    [UI_TRANSLATION_KEYS.home.flashSaleTitle]: "限时抢购",
    [UI_TRANSLATION_KEYS.home.bottomPlaceholder]: "-- 我是底部提示 --",
    [UI_TRANSLATION_KEYS.login.phoneNumberLabel]: "手机号",
    [UI_TRANSLATION_KEYS.login.phoneNumberPlaceholder]: "请输入手机号",
    [UI_TRANSLATION_KEYS.login.passwordLabel]: "密码",
    [UI_TRANSLATION_KEYS.login.passwordPlaceholder]: "请输入密码",
    [UI_TRANSLATION_KEYS.login.submitButton]: "登录",
    [UI_TRANSLATION_KEYS.login.agreePrefix]: "我已阅读并同意",
    [UI_TRANSLATION_KEYS.login.termsLink]: "服务条款",
    [UI_TRANSLATION_KEYS.login.privacyLink]: "隐私政策",
    [UI_TRANSLATION_KEYS.register.usernameLabel]: "用户名",
    [UI_TRANSLATION_KEYS.register.usernamePlaceholder]: "请输入用户名",
    [UI_TRANSLATION_KEYS.register.phoneNumberLabel]: "手机号",
    [UI_TRANSLATION_KEYS.register.phoneNumberPlaceholder]: "请输入手机号",
    [UI_TRANSLATION_KEYS.register.passwordLabel]: "密码",
    [UI_TRANSLATION_KEYS.register.passwordPlaceholder]: "请输入密码",
    [UI_TRANSLATION_KEYS.register.confirmPasswordLabel]: "确认密码",
    [UI_TRANSLATION_KEYS.register.confirmPasswordPlaceholder]: "请再次输入密码",
    [UI_TRANSLATION_KEYS.register.languagePreferenceLabel]: "语言偏好",
    [UI_TRANSLATION_KEYS.register.submitButton]: "注册",
    [UI_TRANSLATION_KEYS.register.agreePrefix]: "我已阅读并同意",
    [UI_TRANSLATION_KEYS.register.termsLink]: "服务条款",
    [UI_TRANSLATION_KEYS.register.privacyLink]: "隐私政策",
    [UI_TRANSLATION_KEYS.messages.phoneRequired]: "手机号不能为空。",
    [UI_TRANSLATION_KEYS.messages.passwordRequired]: "密码不能为空。",
    [UI_TRANSLATION_KEYS.messages.passwordTooShort]: "密码长度不能少于6位。",
    [UI_TRANSLATION_KEYS.messages.passwordMismatch]: "两次输入的密码不一致。",
    [UI_TRANSLATION_KEYS.messages.usernameRequired]: "用户名不能为空。",
    [UI_TRANSLATION_KEYS.messages.agreeTerms]: "请同意服务条款和隐私政策。",
    [UI_TRANSLATION_KEYS.common.quantityLabel]: "数量：",
    [UI_TRANSLATION_KEYS.common.addToCart]: "加入购物车",
    [UI_TRANSLATION_KEYS.common.buyNow]: "立即购买",
    [UI_TRANSLATION_KEYS.common.shoppingCart]: "购物车",
    [UI_TRANSLATION_KEYS.common.soldPrefix]: "已售",
    [UI_TRANSLATION_KEYS.common.buyButton]: "购买",
    [UI_TRANSLATION_KEYS.common.cartUpdateSuccess]: "购物车更新成功",
    [UI_TRANSLATION_KEYS.common.cartUpdateFailed]: "购物车更新失败",
    [UI_TRANSLATION_KEYS.common.cartAddSuccess]: "商品已成功加入购物车",
    [UI_TRANSLATION_KEYS.common.cartAddFailed]: "添加商品到购物车失败",
    [UI_TRANSLATION_KEYS.common.selectProductsFirst]: "请至少选择一件商品",
    [UI_TRANSLATION_KEYS.common.orderSubmitFailed]: "订单提交失败",
    [UI_TRANSLATION_KEYS.common.logoutSuccess]: "退出登录成功",
    [UI_TRANSLATION_KEYS.common.logoutFailed]: "退出登录失败",
    [UI_TRANSLATION_KEYS.common.requestFailed]: "请求失败，请稍后重试",
    [UI_TRANSLATION_KEYS.common.bottomLine]: "-- 我是底部提示 --",
    [UI_TRANSLATION_KEYS.search.placeholder]: "请输入商品名称",
    [UI_TRANSLATION_KEYS.search.historyTitle]: "历史搜索",
    [UI_TRANSLATION_KEYS.search.hotTitle]: "热门搜索",
    [UI_TRANSLATION_KEYS.searchList.placeholder]: "请输入商品名称",
    [UI_TRANSLATION_KEYS.searchList.tabDefault]: "默认",
    [UI_TRANSLATION_KEYS.searchList.tabSale]: "销量",
    [UI_TRANSLATION_KEYS.searchList.tabPrice]: "价格",
    [UI_TRANSLATION_KEYS.searchList.sold]: "已售",
    [UI_TRANSLATION_KEYS.nearby.title]: "切换门店",
    [UI_TRANSLATION_KEYS.nearby.searchPlaceholder]: "请输入地址",
    [UI_TRANSLATION_KEYS.nearby.subtitle]: "附近门店",
    [UI_TRANSLATION_KEYS.nearby.phoneLabel]: "电话：",
    [UI_TRANSLATION_KEYS.order.loading]: "加载中...",
    [UI_TRANSLATION_KEYS.order.title]: "确认订单",
    [UI_TRANSLATION_KEYS.order.receiverLabel]: "收货人：",
    [UI_TRANSLATION_KEYS.order.addressLabel]: "地址：",
    [UI_TRANSLATION_KEYS.order.deliveryTime]: "配送时间",
    [UI_TRANSLATION_KEYS.order.totalLabel]: "合计：",
    [UI_TRANSLATION_KEYS.order.placeOrderButton]: "提交订单",
    [UI_TRANSLATION_KEYS.order.chooseAddressTitle]: "选择地址",
    [UI_TRANSLATION_KEYS.order.choosePaymentTitle]: "选择支付方式",
    [UI_TRANSLATION_KEYS.order.paymentWeChat]: "微信支付",
    [UI_TRANSLATION_KEYS.order.paymentBalance]: "余额",
    [UI_TRANSLATION_KEYS.order.payNowButton]: "立即支付",
    [UI_TRANSLATION_KEYS.order.detailFailed]: "获取订单详情失败",
    [UI_TRANSLATION_KEYS.order.addressListFailed]: "获取地址列表失败",
    [UI_TRANSLATION_KEYS.order.paymentSuccess]: "支付成功！",
    [UI_TRANSLATION_KEYS.order.paymentFailed]: "支付失败",
    [UI_TRANSLATION_KEYS.category.pageTitle]: "分类",
    [UI_TRANSLATION_KEYS.category.searchPlaceholder]: "请输入商品名称",
    [UI_TRANSLATION_KEYS.category.allProductsTab]: "全部商品",
    [UI_TRANSLATION_KEYS.category.allTag]: "全部",
    [UI_TRANSLATION_KEYS.category.featuredProducts]: "精选好物",
    [UI_TRANSLATION_KEYS.detail.pageTitle]: "详情",
    [UI_TRANSLATION_KEYS.detail.specTitle]: "规格信息",
    [UI_TRANSLATION_KEYS.detail.originLabel]: "产地",
    [UI_TRANSLATION_KEYS.detail.specLabel]: "规格",
    [UI_TRANSLATION_KEYS.detail.detailSectionTitle]: "商品详情",
    [UI_TRANSLATION_KEYS.cart.pageTitle]: "购物车",
    [UI_TRANSLATION_KEYS.cart.selectAll]: "全选",
    [UI_TRANSLATION_KEYS.cart.total]: "合计：",
    [UI_TRANSLATION_KEYS.cart.checkout]: "去结算",
    [UI_TRANSLATION_KEYS.profile.pageTitle]: "我的",
    [UI_TRANSLATION_KEYS.profile.logout]: "退出登录",
    [UI_TRANSLATION_KEYS.profile.member]: "会员",
    [UI_TRANSLATION_KEYS.profile.coupons]: "优惠券",
    [UI_TRANSLATION_KEYS.profile.rewardPoints]: "积分",
    [UI_TRANSLATION_KEYS.profile.allOrders]: "全部订单",
    [UI_TRANSLATION_KEYS.profile.pendingPayment]: "待付款",
    [UI_TRANSLATION_KEYS.profile.awaitingShipment]: "待发货",
    [UI_TRANSLATION_KEYS.profile.awaitingDelivery]: "待收货",
    [UI_TRANSLATION_KEYS.profile.returnsRefunds]: "退换/售后",
    [UI_TRANSLATION_KEYS.profile.customerService]: "客服中心",
    [UI_TRANSLATION_KEYS.profile.settings]: "设置",
    [UI_TRANSLATION_KEYS.profile.address]: "地址管理"
  },
  [LANGUAGE_FR_FR]: {
    [UI_TRANSLATION_KEYS.nav.home]: "Accueil",
    [UI_TRANSLATION_KEYS.nav.category]: "Catégorie",
    [UI_TRANSLATION_KEYS.nav.cart]: "Panier",
    [UI_TRANSLATION_KEYS.nav.profile]: "Profil",
    [UI_TRANSLATION_KEYS.home.newProductTitle]: "Nouveautés",
    [UI_TRANSLATION_KEYS.home.flashSaleTitle]: "Vente flash",
    [UI_TRANSLATION_KEYS.home.bottomPlaceholder]: "-- Ceci est le bas de page --",
    [UI_TRANSLATION_KEYS.login.phoneNumberLabel]: "numéro de téléphone",
    [UI_TRANSLATION_KEYS.login.phoneNumberPlaceholder]: "veuillez saisir le numéro de téléphone",
    [UI_TRANSLATION_KEYS.login.passwordLabel]: "mot de passe",
    [UI_TRANSLATION_KEYS.login.passwordPlaceholder]: "veuillez saisir le mot de passe",
    [UI_TRANSLATION_KEYS.login.submitButton]: "connexion",
    [UI_TRANSLATION_KEYS.login.agreePrefix]: "J’accepte les",
    [UI_TRANSLATION_KEYS.login.termsLink]: "Conditions générales",
    [UI_TRANSLATION_KEYS.login.privacyLink]: "Politique de confidentialité",
    [UI_TRANSLATION_KEYS.register.usernameLabel]: "nom d’utilisateur",
    [UI_TRANSLATION_KEYS.register.usernamePlaceholder]: "veuillez saisir le nom d’utilisateur",
    [UI_TRANSLATION_KEYS.register.phoneNumberLabel]: "numéro de téléphone",
    [UI_TRANSLATION_KEYS.register.phoneNumberPlaceholder]: "veuillez saisir le numéro de téléphone",
    [UI_TRANSLATION_KEYS.register.passwordLabel]: "mot de passe",
    [UI_TRANSLATION_KEYS.register.passwordPlaceholder]: "veuillez saisir le mot de passe",
    [UI_TRANSLATION_KEYS.register.confirmPasswordLabel]: "confirmer le mot de passe",
    [UI_TRANSLATION_KEYS.register.confirmPasswordPlaceholder]: "veuillez ressaisir le mot de passe",
    [UI_TRANSLATION_KEYS.register.languagePreferenceLabel]: "Préférence de langue",
    [UI_TRANSLATION_KEYS.register.submitButton]: "inscription",
    [UI_TRANSLATION_KEYS.register.agreePrefix]: "J’accepte les",
    [UI_TRANSLATION_KEYS.register.termsLink]: "Conditions générales",
    [UI_TRANSLATION_KEYS.register.privacyLink]: "Politique de confidentialité",
    [UI_TRANSLATION_KEYS.messages.phoneRequired]: "le numéro de téléphone ne doit pas être vide.",
    [UI_TRANSLATION_KEYS.messages.passwordRequired]: "le mot de passe ne doit pas être vide.",
    [UI_TRANSLATION_KEYS.messages.passwordTooShort]: "la longueur du mot de passe doit être d’au moins 6 caractères.",
    [UI_TRANSLATION_KEYS.messages.passwordMismatch]: "les mots de passe doivent être identiques.",
    [UI_TRANSLATION_KEYS.messages.usernameRequired]: "le nom d’utilisateur ne doit pas être vide.",
    [UI_TRANSLATION_KEYS.messages.agreeTerms]: "Veuillez accepter les conditions générales et la politique de confidentialité.",
    [UI_TRANSLATION_KEYS.common.quantityLabel]: "Quantité :",
    [UI_TRANSLATION_KEYS.common.addToCart]: "Ajouter au panier",
    [UI_TRANSLATION_KEYS.common.buyNow]: "Acheter maintenant",
    [UI_TRANSLATION_KEYS.common.shoppingCart]: "Panier",
    [UI_TRANSLATION_KEYS.common.soldPrefix]: "vendus",
    [UI_TRANSLATION_KEYS.common.buyButton]: "acheter",
    [UI_TRANSLATION_KEYS.common.cartUpdateSuccess]: "Panier mis à jour avec succès !",
    [UI_TRANSLATION_KEYS.common.cartUpdateFailed]: "Échec de la mise à jour du panier",
    [UI_TRANSLATION_KEYS.common.cartAddSuccess]: "Produit ajouté au panier",
    [UI_TRANSLATION_KEYS.common.cartAddFailed]: "Échec de l'ajout du produit au panier",
    [UI_TRANSLATION_KEYS.common.selectProductsFirst]: "Sélectionnez au moins un produit",
    [UI_TRANSLATION_KEYS.common.orderSubmitFailed]: "Échec de la soumission de la commande",
    [UI_TRANSLATION_KEYS.common.logoutSuccess]: "Déconnexion réussie",
    [UI_TRANSLATION_KEYS.common.logoutFailed]: "Échec de la déconnexion",
    [UI_TRANSLATION_KEYS.common.requestFailed]: "Échec de la requête, veuillez réessayer",
    [UI_TRANSLATION_KEYS.common.bottomLine]: "-- Ceci est le bas de page --",
    [UI_TRANSLATION_KEYS.search.placeholder]: "Veuillez saisir le nom du produit",
    [UI_TRANSLATION_KEYS.search.historyTitle]: "Recherche historique",
    [UI_TRANSLATION_KEYS.search.hotTitle]: "Recherche populaire",
    [UI_TRANSLATION_KEYS.searchList.placeholder]: "Veuillez saisir le nom du produit",
    [UI_TRANSLATION_KEYS.searchList.tabDefault]: "Par défaut",
    [UI_TRANSLATION_KEYS.searchList.tabSale]: "Ventes",
    [UI_TRANSLATION_KEYS.searchList.tabPrice]: "Prix",
    [UI_TRANSLATION_KEYS.searchList.sold]: "vendus",
    [UI_TRANSLATION_KEYS.nearby.title]: "Changer de magasin",
    [UI_TRANSLATION_KEYS.nearby.searchPlaceholder]: "Veuillez saisir l'adresse",
    [UI_TRANSLATION_KEYS.nearby.subtitle]: "Magasin à proximité",
    [UI_TRANSLATION_KEYS.nearby.phoneLabel]: "Tél：",
    [UI_TRANSLATION_KEYS.order.loading]: "Chargement...",
    [UI_TRANSLATION_KEYS.order.title]: "Confirmer la commande",
    [UI_TRANSLATION_KEYS.order.receiverLabel]: "Destinataire :",
    [UI_TRANSLATION_KEYS.order.addressLabel]: "Adresse :",
    [UI_TRANSLATION_KEYS.order.deliveryTime]: "Heure de livraison",
    [UI_TRANSLATION_KEYS.order.totalLabel]: "Total :",
    [UI_TRANSLATION_KEYS.order.placeOrderButton]: "Passer la commande",
    [UI_TRANSLATION_KEYS.order.chooseAddressTitle]: "Choisir l'adresse",
    [UI_TRANSLATION_KEYS.order.choosePaymentTitle]: "Choisir le mode de paiement",
    [UI_TRANSLATION_KEYS.order.paymentWeChat]: "WeChat",
    [UI_TRANSLATION_KEYS.order.paymentBalance]: "Solde",
    [UI_TRANSLATION_KEYS.order.payNowButton]: "Payer maintenant",
    [UI_TRANSLATION_KEYS.order.detailFailed]: "Échec de la récupération des détails de la commande",
    [UI_TRANSLATION_KEYS.order.addressListFailed]: "Échec de la récupération de la liste d'adresses",
    [UI_TRANSLATION_KEYS.order.paymentSuccess]: "Paiement réussi !",
    [UI_TRANSLATION_KEYS.order.paymentFailed]: "Échec du paiement",
    [UI_TRANSLATION_KEYS.category.pageTitle]: "Catégorie",
    [UI_TRANSLATION_KEYS.category.searchPlaceholder]: "Veuillez saisir le nom du produit",
    [UI_TRANSLATION_KEYS.category.allProductsTab]: "Tous les produits",
    [UI_TRANSLATION_KEYS.category.allTag]: "Tous",
    [UI_TRANSLATION_KEYS.category.featuredProducts]: "Produits vedettes",
    [UI_TRANSLATION_KEYS.detail.pageTitle]: "Détail",
    [UI_TRANSLATION_KEYS.detail.specTitle]: "Informations sur les spécifications",
    [UI_TRANSLATION_KEYS.detail.originLabel]: "origine",
    [UI_TRANSLATION_KEYS.detail.specLabel]: "spécification",
    [UI_TRANSLATION_KEYS.detail.detailSectionTitle]: "détails du produit",
    [UI_TRANSLATION_KEYS.cart.pageTitle]: "Panier",
    [UI_TRANSLATION_KEYS.cart.selectAll]: "Tout sélectionner",
    [UI_TRANSLATION_KEYS.cart.total]: "Total :",
    [UI_TRANSLATION_KEYS.cart.checkout]: "Valider",
    [UI_TRANSLATION_KEYS.profile.pageTitle]: "Profil",
    [UI_TRANSLATION_KEYS.profile.logout]: "Se déconnecter",
    [UI_TRANSLATION_KEYS.profile.member]: "Membre",
    [UI_TRANSLATION_KEYS.profile.coupons]: "Coupons",
    [UI_TRANSLATION_KEYS.profile.rewardPoints]: "Points de fidélité",
    [UI_TRANSLATION_KEYS.profile.allOrders]: "Toutes les commandes",
    [UI_TRANSLATION_KEYS.profile.pendingPayment]: "Paiement en attente",
    [UI_TRANSLATION_KEYS.profile.awaitingShipment]: "En attente d'expédition",
    [UI_TRANSLATION_KEYS.profile.awaitingDelivery]: "En attente de livraison",
    [UI_TRANSLATION_KEYS.profile.returnsRefunds]: "Retours & remboursements",
    [UI_TRANSLATION_KEYS.profile.customerService]: "Service client",
    [UI_TRANSLATION_KEYS.profile.settings]: "Paramètres",
    [UI_TRANSLATION_KEYS.profile.address]: "Adresse"
  }
};

const DEFAULT_UI_TRANSLATIONS = UI_TRANSLATIONS[DEFAULT_LANGUAGE];

/**
 * 获取当前语言偏好
 * 优先级：用户设置的偏好 > localStorage > 默认语言
 */
export function getCurrentLanguage(): string {
  const storedLanguage = localStorage.getItem(STORAGE_LANGUAGE);
  if (storedLanguage && isValidLanguage(storedLanguage)) {
    return storedLanguage;
  }
  return DEFAULT_LANGUAGE;
}

/**
 * 验证语言代码是否有效
 */
export function isValidLanguage(languageCode: string): boolean {
  return SUPPORTED_LANGUAGES.includes(languageCode);
}

function dispatchLanguageChange(language: string, previousLanguage: string): void {
  if (typeof window === "undefined" || typeof CustomEvent === "undefined") {
    return;
  }

  const event: CustomEvent<LanguageChangeDetail> = new CustomEvent(LANGUAGE_CHANGE_EVENT, {
    detail: { language, previousLanguage }
  });
  window.dispatchEvent(event);
}

/**
 * 设置语言偏好
 */
export function setLanguagePreference(languageCode: string): void {
  const previousLanguage = getCurrentLanguage();
  const normalizedLanguage = isValidLanguage(languageCode) ? languageCode : DEFAULT_LANGUAGE;
  localStorage.setItem(STORAGE_LANGUAGE, normalizedLanguage);
  dispatchLanguageChange(normalizedLanguage, previousLanguage);
}

/**
 * 订阅语言变更事件
 */
export function subscribeLanguageChange(handler: LanguageChangeHandler): () => void {
  if (typeof window === "undefined") {
    return () => undefined;
  }

  const listener = (event: Event) => {
    const languageEvent = event as CustomEvent<LanguageChangeDetail>;
    handler(languageEvent.detail.language, languageEvent.detail.previousLanguage);
  };

  window.addEventListener(LANGUAGE_CHANGE_EVENT, listener as EventListener);
  return () => {
    window.removeEventListener(LANGUAGE_CHANGE_EVENT, listener as EventListener);
  };
}

/**
 * 获取语言名称
 */
export function getLanguageName(languageCode: string): string {
  const option = LANGUAGE_OPTIONS.find(opt => opt.code === languageCode);
  return option ? option.name : "English";
}

/**
 * 获取本地语言名称
 */
export function getNativeLanguageName(languageCode: string): string {
  const option = LANGUAGE_OPTIONS.find(opt => opt.code === languageCode);
  return option ? option.nativeName : "English";
}

/**
 * 翻译UI文案
 */
export function translate(key: UiTranslationKey | string, languageCode?: string): string {
  const targetLanguage = languageCode && isValidLanguage(languageCode) ? languageCode : getCurrentLanguage();
  const translations = UI_TRANSLATIONS[targetLanguage] || {};
  if (translations[key]) {
    return translations[key];
  }
  return (DEFAULT_UI_TRANSLATIONS && DEFAULT_UI_TRANSLATIONS[key]) || key;
}

/**
 * 从用户数据中获取语言偏好（用于登录后同步）
 */
export function syncLanguageFromUser(userLanguage?: string): void {
  if (userLanguage && isValidLanguage(userLanguage)) {
    setLanguagePreference(userLanguage);
  }
}

/**
 * 清除语言偏好（用于登出时）
 */
export function clearLanguagePreference(): void {
  const previousLanguage = getCurrentLanguage();
  localStorage.removeItem(STORAGE_LANGUAGE);
  dispatchLanguageChange(DEFAULT_LANGUAGE, previousLanguage);
}