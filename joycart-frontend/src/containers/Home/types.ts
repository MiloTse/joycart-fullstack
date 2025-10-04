

//位置信息类型, an object
export type LocationType = {
    id: string,
    address: string
}

//轮播图信息类型 Array
export type BannersType = Array<{
    id: string,
    imgUrl: string
}>

//分类信息类型
export type CategoriesType = Array<{
    id: string,
    name: string,
    imgUrl: string
}>



//列表信息类型
export type CardListType = Array<{
    id: string,
    name: string,
    title: string,
    subtitle: string,
    imgUrl: string,
    price:string
}>


//返回内容类型 - 适配ResponseDTO格式
export type ResponseType = {
    code: number;
    message: string;
    data: {
        location: LocationType;
        banners: BannersType;
        categories: CategoriesType;
        fresh: CardListType;
    }
}
