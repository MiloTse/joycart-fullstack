//返回内容类型 (已更新为ResponseDTO格式)
export type ResponseType = {
    code: number,
    message: string,
    data: Array<{
        id: string;
        imgUrl: string;
        title: string;
        price: number;
        sales: number;
    }>
}
