import Head from "next/head";
import Image from "next/image";
import type { FC, ReactElement } from "react";
import UserGuest from "../layouts/UserGuest";
import { Formik, Field, Form, FormikHelpers } from "formik";

interface Values {
  email: string;
  password: string;
}

const Home = () => {
  return (
    <div className="relative z-10 w-full h-full flex items-center justify-center">
      <Formik
        initialValues={{
          email: "",
          password: "",
        }}
        onSubmit={(
          values: Values,
          { setSubmitting }: FormikHelpers<Values>
        ) => {
          setTimeout(() => {
            alert(JSON.stringify(values, null, 2));
            setSubmitting(false);
          }, 500);
        }}
      >
        <div className="w-full max-w-md">
          <Form className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
            {/* <div className="w-full">
              <Image
                src={`/static/img/logo.png`}
                alt="logo-hcmute"
                objectFit="fill"
                width={350}
                height={250}
              />
            </div> */}
            <h1 className="text-gray-600 uppercase font-black text-2xl text-center">Đăng Nhập</h1>
            <div className="mb-4">
              <label
                className="block text-gray-700 text-sm font-bold mb-2"
                htmlFor="email"
              >
                Email
              </label>
              <Field
                id="email"
                name="email"
                placeholder="John"
                className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              />
            </div>
            <div className="mb-4">
              <label
                className="block text-gray-700 text-sm font-bold mb-2"
                htmlFor="password"
              >
                Mật khẩu
              </label>
              <Field
                id="password"
                name="password"
                placeholder="John"
                className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              />
            </div>
            <div className="flex items-center justify-between">
              <button
                className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
                type="submit"
              >
                Đăng nhập
              </button>
              <a
                className="inline-block align-baseline font-bold text-sm text-blue-500 hover:text-blue-800"
                href="#"
              >
                Quên mật khẩu?
              </a>
            </div>
          </Form>
        </div>
      </Formik>
    </div>
  );
};
Home.getLayout = function getLayout(page: ReactElement) {
  return <UserGuest>{page}</UserGuest>;
};
export default Home;
