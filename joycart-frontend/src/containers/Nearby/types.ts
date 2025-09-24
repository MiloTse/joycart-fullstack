//返回内容类型 - 适配ResponseDTO格式
export type ResponseType = {
    code: number;
    message: string;
    data: Array<{
        id: string;
        name: string;
        phone: string;
        address: string;
        distance: string;
        longitude: string;
        latitude: string;
    }>
}