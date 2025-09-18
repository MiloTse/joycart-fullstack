//返回内容类型
export type ResponseType = {
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